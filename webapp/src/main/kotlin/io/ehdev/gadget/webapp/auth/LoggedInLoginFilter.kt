package io.ehdev.gadget.webapp.auth

import io.ehdev.gadget.model.GadgetPrincipal
import io.ehdev.gadget.model.lazyLogger
import io.ehdev.gadget.webapp.configuration.ApplicationConfig
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.security.Principal
import java.util.Optional

@Order(0)
class LoggedInLoginFilter(environment: Environment, private val applicationConfig: ApplicationConfig) : WebFilter {

    private val log by lazyLogger()

    private val managementPort = environment.getProperty("management.server.port", "-2").toInt()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        if (exchange.request.uri.port == managementPort) {
            return chain.filter(exchange)
        }

        return if (exchange.request.path.toString().startsWith("/gadget")) {
            exchange.getPrincipal<Principal>()
                    .defaultIfEmpty(GadgetPrincipal.AnonymousManagerPrincipal())
                    .flatMap {
                        when (it) {
                            is GadgetPrincipal.AccountManagerPrincipal -> return@flatMap chain.filter(exchange)
                            else -> {
                                val redirectUri = UriComponentsBuilder.fromUriString(applicationConfig.defaultLogin)
                                        .queryParam("redirectTo", exchange.request.uri.toString())
                                        .build()

                                log.info("Redirect to {}", redirectUri)

                                exchange.response.statusCode = HttpStatus.TEMPORARY_REDIRECT
                                exchange.response.headers.location = redirectUri.toUri()
                                return@flatMap exchange.response.writeWith(Mono.justOrEmpty(Optional.empty()))
                            }
                        }
                    }
        } else {
            chain.filter(exchange)
        }
    }
}