package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.webapp.api.model.RedirectResponseModel
import io.ehdev.gadget.webapp.configuration.findScheme
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

interface RedirectResource {
    fun doRedirect(request: ServerRequest): Mono<ServerResponse>

    fun showRedirectJson(request: ServerRequest): Mono<ServerResponse>
}

open class DefaultRedirectResource(private val redirectManager: RedirectManager) : RedirectResource {

    override fun doRedirect(request: ServerRequest): Mono<ServerResponse> {
        val requestPath = request.pathVariable("path").trim('/')
        val redirectUrl = GadgetUtil.findRequestRedirect(redirectManager, requestPath)

        return if (redirectUrl == null) {
            val searchUri = request.uriBuilder()
                    .replacePath("/gadget/search")
                    .replaceQuery("")
                    .queryParam("searchString", requestPath)
                    .scheme(request.findScheme())
                    .build()
            ServerResponse.temporaryRedirect(searchUri).build()
        } else {
            ServerResponse.temporaryRedirect(URI.create(redirectUrl)).build()
        }
    }

    override fun showRedirectJson(request: ServerRequest): Mono<ServerResponse> {
        val requestPath = request.pathVariable("path").trim('/')
        val redirectUrl = GadgetUtil.findRequestRedirect(redirectManager, requestPath)
        return if (redirectUrl == null) {
            ServerResponse.notFound().build()
        } else {
            val model = RedirectResponseModel(requestPath, redirectUrl)
            ServerResponse.ok().body(Mono.just(model), RedirectResponseModel::class.java)
        }
    }
}