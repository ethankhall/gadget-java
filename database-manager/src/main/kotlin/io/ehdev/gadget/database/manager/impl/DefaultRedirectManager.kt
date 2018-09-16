package io.ehdev.gadget.database.manager.impl

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.database.manager.api.SearchResults
import io.ehdev.gadget.database.manager.exception.RowChangedInTheBackgroundException
import io.ehdev.gadget.db.Tables
import io.ehdev.gadget.model.RedirectContainer
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Record2
import org.jooq.impl.DSL
import java.time.Clock
import java.time.Instant
import java.util.concurrent.CompletionStage

class DefaultRedirectManager(private val context: DSLContext, private val clock: Clock) : RedirectManager {

    private fun <T> DSLContext.transactionWithDsl(body: (DSLContext) -> T): T {
        return body.invoke(this)
    }

    override fun setRedirect(aliasPath: String, destination: String, userName: String) {
        val variables = RedirectContainer.extractVariables(destination)
        setRedirect(aliasPath, variables, destination, userName)
    }

    override fun setRedirect(aliasPath: String, variables: List<String>, destination: String, userName: String) {
        context.transactionWithDsl { dsl ->
            val existingRow = dsl.select(redirectTable.REDIRECT_ID, redirectTable.UPDATE_COUNT)
                    .from(redirectTable)
                    .where(redirectTable.ALIAS.eq(aliasPath))
                    .fetchOne()

            if (existingRow == null) {
                insertNewRecord(dsl, aliasPath, variables, destination, userName)
            } else {
                updateRedirect(dsl, existingRow, aliasPath, variables, destination, userName)
            }
        }
    }

    private fun updateRedirect(dsl: DSLContext, existingRow: Record2<Long, Long>, aliasPath: String, variables: List<String>, destination: String, userName: String) {
        val updateCount = existingRow.get(redirectTable.UPDATE_COUNT)
        val id = existingRow.get(redirectTable.REDIRECT_ID)
        val now = clock.instant()

        moveRedirectToArchive(dsl, now, existingRow)

        val rows = dsl.update(redirectTable)
                .set(mapOf(redirectTable.ALIAS to aliasPath,
                        redirectTable.VARIABLES to variables.joinToString(","),
                        redirectTable.UPDATE_COUNT to updateCount + 1,
                        redirectTable.CREATED_AT to now,
                        redirectTable.USER to userName,
                        redirectTable.DESTINATION to destination))
                .where(redirectTable.REDIRECT_ID.eq(id), redirectTable.UPDATE_COUNT.eq(updateCount))
                .execute()

        if (rows != 1) {
            throw RowChangedInTheBackgroundException()
        }
    }

    private fun insertNewRecord(dsl: DSLContext, aliasPath: String, variables: List<String>, destination: String, userName: String) {
        val rows = dsl.insertInto(redirectTable)
                .set(mapOf(redirectTable.ALIAS to aliasPath,
                        redirectTable.VARIABLES to variables.joinToString(","),
                        redirectTable.UPDATE_COUNT to 1,
                        redirectTable.CREATED_AT to clock.instant(),
                        redirectTable.USER to userName,
                        redirectTable.DESTINATION to destination))
                .execute()

        if (rows != 1) {
            throw RowChangedInTheBackgroundException()
        }
    }

    private fun moveRedirectToArchive(dsl: DSLContext, now: Instant, existingRow: Record2<Long, Long>) {
        val updateCount = existingRow.get(redirectTable.UPDATE_COUNT)
        val id = existingRow.get(redirectTable.REDIRECT_ID)

        val previousRow = dsl.select().from(redirectTable)
                .where(redirectTable.REDIRECT_ID.eq(id), redirectTable.UPDATE_COUNT.eq(updateCount))
                .fetchOne()

        previousRow ?: throw RowChangedInTheBackgroundException()

        dsl.insertInto(historyTable)
                .set(mapOf(
                        historyTable.ALIAS to previousRow.get(redirectTable.ALIAS),
                        historyTable.VARIABLES to previousRow.get(redirectTable.VARIABLES),
                        historyTable.USER to previousRow.get(redirectTable.USER),
                        historyTable.DESTINATION to previousRow.get(redirectTable.DESTINATION),
                        historyTable.DELETED_AT to now
                ))
    }

    override fun getRedirect(aliasPath: String): CompletionStage<RedirectContainer?> {
        return context.select().from(redirectTable)
                .where(redirectTable.ALIAS.eq(aliasPath))
                .fetchAsync()
                .thenApply { rows ->
                    val row = rows.firstOrNull() ?: return@thenApply null
                    convertToRedirectContainer(row)
                }
    }

    private fun convertToRedirectContainer(row: Record): RedirectContainer {
        val variables = (row.get(redirectTable.VARIABLES) ?: "").split(",")
        return RedirectContainer(row.get(redirectTable.ALIAS), variables, row.get(redirectTable.DESTINATION))
    }

    override fun removeRedirect(aliasPath: String): Boolean {
        return context.transactionWithDsl { dsl ->
            val existingRow = dsl.select(redirectTable.REDIRECT_ID, redirectTable.UPDATE_COUNT)
                    .from(redirectTable)
                    .where(redirectTable.ALIAS.eq(aliasPath))
                    .fetchOne()

            if (existingRow == null) {
                return@transactionWithDsl false
            } else {
                val updateCount = existingRow.get(redirectTable.UPDATE_COUNT)
                val id = existingRow.get(redirectTable.REDIRECT_ID)
                moveRedirectToArchive(dsl, clock.instant(), existingRow)
                dsl.deleteFrom(redirectTable)
                        .where(redirectTable.REDIRECT_ID.eq(id), redirectTable.UPDATE_COUNT.eq(updateCount))
                        .execute() == 1
            }
        }
    }

    override fun searchFor(rootPath: String, offset: Int, size: Int): CompletionStage<SearchResults> {
        val whereClause = redirectTable.ALIAS.like("%$rootPath%")

        val countAsync = context.select(DSL.count())
                .from(redirectTable)
                .where(whereClause)
                .fetchAsync()

        val foundAsync = context.select(redirectTable.ALIAS, redirectTable.VARIABLES, redirectTable.DESTINATION)
                .from(redirectTable)
                .where(whereClause)
                .limit(size)
                .offset(offset)
                .fetchAsync()

        return countAsync.thenCombineAsync(foundAsync) { count, found ->
            val results = found.map { convertToRedirectContainer(it) }
            SearchResults(count[0].get(DSL.count()), results)
        }
    }

    companion object {
        private val redirectTable = Tables.REDIRECT
        private val historyTable = Tables.REDIRECT_HISTORY
    }
}