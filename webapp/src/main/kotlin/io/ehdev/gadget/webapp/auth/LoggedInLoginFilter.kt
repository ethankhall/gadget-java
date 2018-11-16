package io.ehdev.gadget.webapp.auth

import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.security.Principal

@Order(0)
class LoggedInLoginFilter(environment: Environment) : WebFilter {

    private val serverPort = environment.getRequiredProperty("server.port").toInt()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if (exchange.request.uri.port != serverPort) {
            return chain.filter(exchange)
        }

        return if ("gadget" == exchange.request.path.subPath(0).value()) {
            exchange.getPrincipal<Principal>().flatMap {
                when (it) {
                    null -> {
                        val redirectUri = UriComponentsBuilder.fromUri(exchange.request.uri)
                                .queryParam("redirectTo", exchange.request.uri.toString())
                                .build()
                        exchange.response.statusCode = HttpStatus.TEMPORARY_REDIRECT
                        exchange.response.headers.location = redirectUri.toUri()
                        Mono.empty<Void>()
                    }
                    else -> chain.filter(exchange)
                }
            }
        } else {
            chain.filter(exchange)
        }
    }
}