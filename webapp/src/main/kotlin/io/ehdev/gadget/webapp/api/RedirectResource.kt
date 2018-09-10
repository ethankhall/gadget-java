package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.webapp.api.model.RedirectResponseModel
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

class RedirectResource(private val redirectManager: RedirectManager) {

    fun doRedirect(request: ServerRequest): Mono<ServerResponse> {
        val requestPath = request.pathVariable("path")
        val redirectUrl = GadgetUtil.findRequestRedirect(redirectManager, requestPath)

        return if (redirectUrl == null) {
            ServerResponse.notFound().build()
        } else {
            ServerResponse.temporaryRedirect(URI.create(redirectUrl)).build()
        }
    }

    fun showRedirectJson(request: ServerRequest): Mono<ServerResponse> {
        val requestPath = request.pathVariable("path")
        val redirectUrl = GadgetUtil.findRequestRedirect(redirectManager, requestPath)
        return if (redirectUrl == null) {
            ServerResponse.notFound().build()
        } else {
            val model = RedirectResponseModel(requestPath, redirectUrl)
            ServerResponse.ok().body(Mono.just(model), RedirectResponseModel::class.java)
        }
    }
}