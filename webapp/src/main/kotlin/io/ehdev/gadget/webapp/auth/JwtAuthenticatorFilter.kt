package io.ehdev.gadget.webapp.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import io.ehdev.gadget.model.AccountManagerPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.TimeUnit


class JwtAuthenticatorFilter(
        accountManagerHost: String,
        private val om: ObjectMapper) : WebFilter {

    private val cache: Cache<String, AccountManagerPrincipal> = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(1_000)
            .build()

    private var httpConnector: ClientHttpConnector = ReactorClientHttpConnector()
    private var webClient = WebClient.builder()
            .clientConnector(httpConnector)
            .baseUrl(accountManagerHost)
            .build()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val credentials = findAuthToken(exchange) ?: return chain.filter(exchange)

        val principal = Runnable { resolveCredentials(credentials) }
        val newExchange = exchange.mutate().principal(Mono.fromRunnable(principal)).build()
        return chain.filter(newExchange)
    }

    internal fun resolveCredentials(credentials: String): AccountManagerPrincipal? {
        val cacheValue = cache.getIfPresent(credentials)
        if (cacheValue == null) {
            return webClient.get().uri("/api/v1/user")
                    .header(HeaderConst.AUTH_HEADER_NAME, credentials)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .timeout(Duration.ofSeconds(3))
                    .retry(3)
                    .flatMap { it ->
                        if (it.statusCode().is2xxSuccessful) {
                            it.bodyToMono(String::class.java)
                                    .map { body: String ->
                                        val json = om.readTree(body)
                                        val email = json.get("email")?.textValue() ?: return@map null
                                        val accountManagerPrincipal = AccountManagerPrincipal(credentials, email)
                                        cache.put(credentials, accountManagerPrincipal)
                                        accountManagerPrincipal
                                    }
                        } else {
                            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                        }
                    }
                    .block()
        } else {
            return cacheValue
        }
    }

    private fun findAuthToken(exchange: ServerWebExchange): String? {
        val request = exchange.request
        val cookieValue = request.cookies.getFirst(HeaderConst.COOKIE_NAME)?.value
        val headerValue = request.headers.getFirst(HeaderConst.AUTH_HEADER_NAME)

        return when {
            headerValue != null -> headerValue
            cookieValue != null -> cookieValue
            else -> null
        }
    }
}