package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.RedirectContainer
import io.ehdev.gadget.webapp.api.model.PrivilegedRedirectResponseModel
import io.ehdev.gadget.webapp.api.model.RedirectDefinition
import io.ehdev.gadget.webapp.api.model.RedirectResponseModel
import io.ehdev.gadget.webapp.api.model.SearchResponseModel
import io.ehdev.gadget.webapp.api.model.SearchResponseModelResult
import io.ehdev.gadget.webapp.configuration.ApplicationConfig
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.net.URI

interface GadgetJsonResource {
    fun createNewEndpoint(request: ServerRequest): Mono<ServerResponse>
    fun searchRedirects(request: ServerRequest): Mono<ServerResponse>
    fun showRedirectJson(request: ServerRequest, includePrivileged: Boolean): Mono<ServerResponse>
    fun makeModel(requestPath: String, container: RedirectContainer?, includePrivileged: Boolean): Mono<ServerResponse>
    fun updateRedirect(request: ServerRequest): Mono<ServerResponse>
    fun deleteRedirect(request: ServerRequest): Mono<ServerResponse>
}

open class DefaultGadgetJsonResource(private val redirectManager: RedirectManager, config: ApplicationConfig) : GadgetJsonResource {

    private val primaryUriBase = config.primaryUriBase
    private data class RedirectCreated(val editUrl: URI)

    override fun createNewEndpoint(request: ServerRequest): Mono<ServerResponse> {
        return request.principal().flatMap { user ->
            when (user) {
                null -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
                else -> {
                    request.bodyToMono(RedirectDefinition::class.java)
                            .map { redirectDefinition ->
                                redirectManager.setRedirect(redirectDefinition.alias,
                                        redirectDefinition.variables ?: emptyList(),
                                        redirectDefinition.destination, user.name)
                                val editUrl = UriComponentsBuilder.fromUriString(primaryUriBase)
                                        .replacePath("/gadget/resource/${redirectDefinition.alias}")
                                        .build()
                                        .toUri()

                                RedirectCreated(editUrl)

                            }.flatMap {
                                ServerResponse.created(it.editUrl).body(Mono.just(it), RedirectCreated::class.java)
                            }
                }
            }
        }
    }

    override fun searchRedirects(request: ServerRequest): Mono<ServerResponse> {
        val parameters = SearchParameters(request)
        return redirectManager.searchFor(parameters.aliasName, parameters.offset(), parameters.page).toCompletableFuture().toMono()
                .flatMap { (count, results) ->
                    val response = SearchResponseModel(count, results.map { SearchResponseModelResult(it) })
                    ServerResponse.ok().body(Mono.just(response), SearchResponseModel::class.java)
                }
    }

    override fun showRedirectJson(request: ServerRequest, includePrivileged: Boolean): Mono<ServerResponse> {
        val requestPath = request.pathVariable("path").trim('/')
        return Mono.fromCompletionStage(GadgetUtil.findRequestRedirect(redirectManager, requestPath))
                .flatMap { makeModel(requestPath, it, includePrivileged) }
    }

    override fun makeModel(requestPath: String, container: RedirectContainer?, includePrivileged: Boolean): Mono<ServerResponse> {
        return if (container == null) {
            ServerResponse.notFound().build()
        } else {
            val redirect = container.buildRedirect(requestPath)
            if (includePrivileged) {
                val model = PrivilegedRedirectResponseModel(requestPath, redirect, container.createdBy)
                ServerResponse.ok().body(Mono.just(model), PrivilegedRedirectResponseModel::class.java)
            } else {
                val model = RedirectResponseModel(requestPath, redirect)
                ServerResponse.ok().body(Mono.just(model), RedirectResponseModel::class.java)
            }
        }
    }

    override fun updateRedirect(request: ServerRequest): Mono<ServerResponse> {
        return request.principal().flatMap { user ->
            when (user) {
                null -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
                else -> {
                    request.bodyToMono(RedirectDefinition::class.java)
                            .flatMap { redirectDefinition ->
                                if (redirectManager.getRedirect(redirectDefinition.alias).toCompletableFuture().get() != null) {
                                    redirectManager.setRedirect(redirectDefinition.alias,
                                            redirectDefinition.variables ?: emptyList(),
                                            redirectDefinition.destination, user.name)
                                    val redirect = UriComponentsBuilder.fromUriString(primaryUriBase)
                                            .replacePath("/gadget/redirect/${redirectDefinition.alias}")
                                            .build()
                                            .toUri()

                                    ServerResponse.temporaryRedirect(redirect).build()
                                } else {
                                    ServerResponse.notFound().build()
                                }
                            }
                }
            }
        }
    }

    override fun deleteRedirect(request: ServerRequest): Mono<ServerResponse> {
        val decidedPath = request.pathVariable("path").trim('/').split(" ")

        redirectManager.removeRedirect(decidedPath.first())

        return ServerResponse.ok().build()
    }
}