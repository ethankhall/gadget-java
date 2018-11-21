package io.ehdev.gadget.webapp.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import io.ehdev.gadget.model.GadgetPrincipal
import io.ehdev.gadget.model.lazyLogger
import org.apache.commons.lang3.StringUtils
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.security.Principal
import java.time.Duration
import java.util.concurrent.TimeUnit

@Order(-10)
class JwtAuthenticatorFilter(accountManagerHost: String, private val om: ObjectMapper) : WebFilter {

    private val log by lazyLogger()

    private val cache: Cache<String, GadgetPrincipal> = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(1_000)
            .build()

    private var httpConnector: ClientHttpConnector = ReactorClientHttpConnector()
    private var webClient = WebClient.builder()
            .clientConnector(httpConnector)
            .baseUrl(accountManagerHost)
            .build()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val credentials = findAuthToken(exchange)

        val principal: Mono<Principal> = if (null != credentials) {
            resolveCredentials(credentials)
        } else {
            Mono.just(GadgetPrincipal.AnonymousManagerPrincipal())
        }

        val newExchange = exchange.mutate().principal(principal).build()
        return chain.filter(newExchange)
    }

    internal fun resolveCredentials(credentials: String): Mono<Principal> {
        val cacheValue = cache.getIfPresent(credentials)
        return if (cacheValue == null) {
            webClient.get().uri("/api/v1/user")
                    .header(HeaderConst.AUTH_HEADER_NAME, credentials)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .timeout(Duration.ofSeconds(3))
                    .retry(3)
                    .doOnError {
                        log.warn("There was an issue making a request to Account Manager!", it)
                    }
                    .flatMap { it -> handleAuthResponse(credentials, it) }
        } else {
            Mono.just(cacheValue)
        }
    }

    private fun handleAuthResponse(credentials: String, response: ClientResponse): Mono<GadgetPrincipal> {
        when {
            response.statusCode().is2xxSuccessful -> {
                return response.bodyToMono(String::class.java)
                        .map { body ->
                            val json = om.readTree(body)
                            val email = json.get("user")?.get("email")?.textValue() ?: return@map null
                            val accountManagerPrincipal = GadgetPrincipal.AccountManagerPrincipal(credentials, email)
                            cache.put(credentials, accountManagerPrincipal)
                            accountManagerPrincipal
                        }
            }
            response.statusCode().isError -> {
                log.warn("There was an issue making a request to Account Manager!", response)
                return Mono.just(GadgetPrincipal.AnonymousManagerPrincipal())
            }
            else -> {
                val principal = GadgetPrincipal.AnonymousManagerPrincipal()
                log.warn("User was not logged in. Given ID ${principal.token}")
                return Mono.just(principal)
            }
        }
    }

    private fun findAuthToken(exchange: ServerWebExchange): String? {
        val request = exchange.request
        val cookieValue = request.cookies.getFirst(HeaderConst.COOKIE_NAME)?.value
        val headerValue = request.headers.getFirst(HeaderConst.AUTH_HEADER_NAME)

        return when {
            headerValue != null -> StringUtils.trimToNull(headerValue)
            cookieValue != null -> StringUtils.trimToNull(cookieValue)
            else -> null
        }
    }
}