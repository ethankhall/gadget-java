package io.ehdev.gadget.database.manager.api

import io.ehdev.gadget.model.RedirectContainer
import java.util.concurrent.CompletionStage

interface RedirectManager {

    fun setRedirect(aliasPath: String, destination: String, userName: String)

    fun getRedirect(aliasPath: String): CompletionStage<RedirectContainer?>

    fun removeRedirect(aliasPath: String): Boolean

    fun searchFor(rootPath: String, offset: Int, size: Int): CompletionStage<SearchResults>
}

data class SearchResults(val totalCount: Int, val results: List<RedirectContainer>)