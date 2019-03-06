package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.webapp.api.model.SearchResponseModel
import io.ehdev.gadget.webapp.api.model.SearchResponseModelResult
import io.ehdev.gadget.webapp.configuration.ApplicationConfig
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

interface GadgetHtmlResource {
    fun getSearchPage(request: ServerRequest): Mono<ServerResponse>
    fun getRootPage(ignored: ServerRequest): Mono<ServerResponse>
    fun getNewPage(request: ServerRequest): Mono<ServerResponse>
    fun getEditPathPage(request: ServerRequest): Mono<ServerResponse>
    fun getDeletePathPage(request: ServerRequest): Mono<ServerResponse>
}

open class DefaultGadgetHtmlResource(private val redirectManager: RedirectManager, config: ApplicationConfig) : GadgetHtmlResource {
    private val primaryUriBase = config.primaryUriBase

    data class PageRedirect(val offset: Int, val url: String) {
        fun getPageNumber() = offset + 1
    }

    override fun getSearchPage(request: ServerRequest): Mono<ServerResponse> {
        val formData = SearchParameters(request)
        val searchResults = redirectManager.searchFor(formData.aliasName, formData.offset(), formData.size)
        return Mono.fromCompletionStage(searchResults)
                .flatMap { (count, results) ->
                    val maxPages = Math.ceil(count.toDouble() / formData.size).toInt()

                    val pages = IntRange(formData.page - 10, formData.page + 10).toList()
                            .filter { it in 1..maxPages }
                            .map {
                                val url = UriComponentsBuilder.fromUriString(primaryUriBase)
                                        .replacePath("/gadget/search")
                                        .queryParam(GadgetUtil.ALIAS, formData.aliasName)
                                        .queryParam("page", it - 1)
                                        .queryParam("size", formData.size)
                                        .build()
                                        .toUriString()
                                PageRedirect(it - 1, url)
                            }

                    val previousPages = pages.filter { it.offset < formData.page }
                    val nextPages = pages.filter { it.offset > formData.page }
                    val currentPage = pages.filter { it.offset == formData.page }

                    val response = SearchResponseModel(count, results.map { SearchResponseModelResult(it) })
                    ServerResponse.ok().render("search-gadget-view",
                            mapOf("name" to formData.aliasName,
                                    "result" to response.results,
                                    "previousPages" to previousPages,
                                    "currentPage" to currentPage,
                                    "nextPages" to nextPages))
                }
    }

    override fun getRootPage(ignored: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().render("root-gadget-view")
    }

    override fun getNewPage(request: ServerRequest): Mono<ServerResponse> {
        val name = request.queryParam("name").orElse("")
        return redirectManager.getRedirect(name).toCompletableFuture().toMono()
                .map { it.redirect }
                .switchIfEmpty(Mono.just(""))
                .flatMap { ServerResponse.ok().render("new-gadget-view", mapOf("name" to name, "destination" to it)) }
    }

    override fun getEditPathPage(request: ServerRequest): Mono<ServerResponse> {
        val name = request.queryParam("name").orElse("")
        return redirectManager.getRedirect(name).toCompletableFuture().toMono()
                .map { it.redirect }
                .switchIfEmpty(Mono.just(""))
                .flatMap { ServerResponse.ok().render("edit-gadget-view", mapOf("name" to name, "destination" to it)) }
    }

    override fun getDeletePathPage(request: ServerRequest): Mono<ServerResponse> {
        val name = request.queryParam("name").orElse("")
        return redirectManager.getRedirect(name).toCompletableFuture().toMono()
                .map { it.redirect }
                .switchIfEmpty(Mono.just(""))
                .flatMap { ServerResponse.ok().render("delete-gadget-view", mapOf("name" to name, "destination" to it)) }
    }
}