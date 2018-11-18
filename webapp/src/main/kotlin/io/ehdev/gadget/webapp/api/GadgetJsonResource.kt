package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.lazyLogger
import io.ehdev.gadget.webapp.api.model.NewRedirect
import io.ehdev.gadget.webapp.api.model.RedirectResponseModel
import io.ehdev.gadget.webapp.api.model.SearchResponseModel
import io.ehdev.gadget.webapp.configuration.findScheme
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

interface GadgetJsonResource {
    fun createNewEndpoint(request: ServerRequest): Mono<ServerResponse>
    fun searchRedirects(request: ServerRequest): Mono<ServerResponse>
}

open class DefaultGadgetJsonResource(private val redirectManager: RedirectManager) : GadgetJsonResource {

    private val log by lazyLogger()

    override fun createNewEndpoint(request: ServerRequest): Mono<ServerResponse> {
        return request.principal().flatMap { user ->
            when (user) {
                null -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
                else -> {
                    request.bodyToMono(NewRedirect::class.java)
                            .map { redirectDefinition ->
                                redirectManager.setRedirect(redirectDefinition.alias,
                                        redirectDefinition.variables ?: emptyList(),
                                        redirectDefinition.destination, user.name)
                                request.uriBuilder()
                                        .replacePath("/gadget/redirect/${redirectDefinition.alias}")
                                        .scheme(request.findScheme())
                                        .build()
                            }.flatMap { ServerResponse.temporaryRedirect(it).build() }
                }
            }
        }
    }

    override fun searchRedirects(request: ServerRequest): Mono<ServerResponse> {
        val parameters = SearchParameters(request)
        return redirectManager.searchFor(parameters.searchString, parameters.offset(), parameters.page).toCompletableFuture().toMono()
                .flatMap { (count, results) ->
                    val response = SearchResponseModel(count, results.map { RedirectResponseModel(it.aliasRoot, it.redirect) })
                    ServerResponse.ok().body(Mono.just(response), SearchResponseModel::class.java)
                }
    }
}