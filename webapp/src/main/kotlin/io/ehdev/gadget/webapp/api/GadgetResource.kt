package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.getLogger
import io.ehdev.gadget.webapp.api.model.NewRedirect
import io.ehdev.gadget.webapp.api.model.RedirectResponseModel
import io.ehdev.gadget.webapp.api.model.SearchResponseModel
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class GadgetResource(private val redirectManager: RedirectManager) {

    private val log by getLogger()

    fun createNewEndpoint(request: ServerRequest): Mono<ServerResponse> {
        return request.principal().flatMap { user ->
            when (user) {
                null -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
                else -> {
                    request.bodyToMono(NewRedirect::class.java)
                            .map { redirectDefinition ->
                                redirectManager.setRedirect(redirectDefinition.alias,
                                        redirectDefinition.variables ?: emptyList(),
                                        redirectDefinition.destination, user.name)
                                request.uriBuilder().replacePath("/gadget/redirect/${redirectDefinition.alias}").build()
                            }.flatMap { ServerResponse.temporaryRedirect(it).build() }
                }
            }
        }
    }

    fun searchRedirects(request: ServerRequest): Mono<ServerResponse> {
        return request.principal().flatMap { user ->
            user ?: return@flatMap ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
            val requestPath = request.pathVariable("path")
            val results = redirectManager.searchFor(requestPath)
            val response = Mono.just(SearchResponseModel(results.map { RedirectResponseModel(it.aliasRoot, it.redirect) }))
            ServerResponse.ok().body(response, SearchResponseModel::class.java)
        }
    }
}