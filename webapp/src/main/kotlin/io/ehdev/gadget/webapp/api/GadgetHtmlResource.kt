package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.lazyLogger
import io.ehdev.gadget.webapp.api.model.RedirectResponseModel
import io.ehdev.gadget.webapp.api.model.SearchResponseModel
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

interface GadgetHtmlResource {
    fun rootPage(request: ServerRequest): Mono<ServerResponse>
    fun searchFor(request: ServerRequest): Mono<ServerResponse>
    fun displayUpdatePage(request: ServerRequest): Mono<ServerResponse>
    fun updateRedirect(request: ServerRequest): Mono<ServerResponse>
}

open class DefaultGadgetHtmlResource(private val redirectManager: RedirectManager) : GadgetHtmlResource {
    private val log by lazyLogger()

    override fun searchFor(request: ServerRequest): Mono<ServerResponse> {
        val formData = SearchParameters(request)
        val future = redirectManager.searchFor(formData.searchString, formData.offset(), formData.size).toCompletableFuture()
        return Mono.fromFuture(future)
                .flatMap { (count, results) ->
                    val response = SearchResponseModel(count, results.map { RedirectResponseModel(it.aliasRoot, it.redirect) })
                    ServerResponse.ok().render("search-gadget-view",
                            mapOf("searchString" to formData.searchString,
                                    "result" to response.results,
                                    "totalCount" to response.count))
                }
    }

    override fun rootPage(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().render("root-gadget-view")
    }

    override fun displayUpdatePage(request: ServerRequest): Mono<ServerResponse> {
        val name = request.queryParam("name").orElse("")
        return redirectManager.getRedirect(name).toCompletableFuture().toMono()
                .map { it.redirect }
                .switchIfEmpty(Mono.just(""))
                .flatMap { ServerResponse.ok().render("update-gadget-view", mapOf("name" to name, "destination" to it)) }
    }

    override fun updateRedirect(request: ServerRequest): Mono<ServerResponse> {
        return request.principal().flatMap { user ->
            request.formData()
                    .flatMap { map ->
                        val name = map.getFirst("name")!!
                        val destination = map.getFirst("destination")!!
                        redirectManager.setRedirect(name, destination, user.name)
                        val destUri = request.uriBuilder()
                                .replacePath("/gadget/search")
                                .replaceQuery("")
                                .queryParam("searchString", name)
                                .build()
                        ServerResponse.seeOther(destUri).build()
                    }
        }
    }
}