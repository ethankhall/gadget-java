package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.webapp.api.GadgetUtil.ALIAS
import io.ehdev.gadget.webapp.configuration.ApplicationConfig
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI

interface RedirectResource {
    fun doRedirect(request: ServerRequest): Mono<ServerResponse>
}

open class DefaultRedirectResource(private val redirectManager: RedirectManager, config: ApplicationConfig) : RedirectResource {

    private val primaryUriBase = config.primaryUriBase

    override fun doRedirect(request: ServerRequest): Mono<ServerResponse> {
        val requestPath = request.pathVariable("path").trim('/')
        val searchUri = UriComponentsBuilder.fromUriString(primaryUriBase)
                .replacePath("/gadget/search")
                .queryParam(ALIAS, requestPath)
                .build()

        return Mono.fromCompletionStage(GadgetUtil.findRequestRedirect(redirectManager, requestPath))
                .flatMap {
            val redirectUrl = it?.buildRedirect(requestPath)
            if (redirectUrl == null) {
                ServerResponse.temporaryRedirect(searchUri.toUri()).build()
            } else {
                ServerResponse.temporaryRedirect(URI.create(redirectUrl)).build()
            }
        }.switchIfEmpty(ServerResponse.temporaryRedirect(searchUri.toUri()).build())
    }
}