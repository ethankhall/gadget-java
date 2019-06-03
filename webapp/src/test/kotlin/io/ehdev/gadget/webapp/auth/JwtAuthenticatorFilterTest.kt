package io.ehdev.gadget.webapp.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.stubbing.Scenario
import io.ehdev.gadget.model.GadgetPrincipal
import io.ehdev.gadget.webapp.configuration.ApplicationConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.Duration


internal class JwtAuthenticatorFilterTest {

    @Test
    fun `will make request to account-manager`() {
        val server = WireMockServer(WireMockConfiguration().dynamicPort())
        server.start()
        server.stubFor(get(urlPathEqualTo("/api/v1/user"))
                .willReturn(aResponse().withBody("""{ "user": { "email": "foo@example.com" } }""")))

        server.start()
        val baseUrl = server.url("/").toString()

        val config = ApplicationConfig()
        config.authHost = baseUrl
        config.disableAuth = false

        val authenticator = JwtAuthenticatorFilter(config, jacksonObjectMapper())
        val authenticate = authenticator.resolveCredentials("fooo").block() as GadgetPrincipal.AccountManagerPrincipal

        assertNotNull(authenticate)
        assertEquals("foo@example.com", authenticate.name)
        assertEquals("fooo", authenticate.token)

        server.stop()
        server.verify(getRequestedFor(urlEqualTo("/api/v1/user"))
                .withHeader(HeaderConst.AUTH_HEADER_NAME, equalTo("fooo")))
    }

    @Test
    fun `will retry three times`() {
        val server = WireMockServer(WireMockConfiguration().dynamicPort())
        server.start()
        server.stubFor(get(urlPathEqualTo("/api/v1/user"))
                .inScenario("Timeout")
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse()
                        .withFixedDelay(Duration.ofSeconds(10).toMillis().toInt()))
                .willSetStateTo("CALL1"))

        server.stubFor(get(urlPathEqualTo("/api/v1/user"))
                .inScenario("Timeout")
                .whenScenarioStateIs("CALL1")
                .willReturn(aResponse().withFixedDelay(Duration.ofSeconds(10).toMillis().toInt()))
                .willSetStateTo("CALL2"))

        server.stubFor(get(urlPathEqualTo("/api/v1/user"))
                .inScenario("Timeout")
                .whenScenarioStateIs("CALL2")
                .willReturn(aResponse().withBody("""{ "user": { "email": "foo@example.com" } }""")))

        val baseUrl = server.url("/").toString()

        val config = ApplicationConfig()
        config.authHost = baseUrl
        config.disableAuth = false

        val authenticator = JwtAuthenticatorFilter(config, jacksonObjectMapper())
        for (i in 0..1) {
            val authenticate = authenticator.resolveCredentials("fooo").block() as GadgetPrincipal.AccountManagerPrincipal

            assertNotNull(authenticate)
            assertEquals("foo@example.com", authenticate.name)
            assertEquals("fooo", authenticate.token)
        }

        server.stop()
        server.verify(3, getRequestedFor(urlEqualTo("/api/v1/user"))
                .withHeader(HeaderConst.AUTH_HEADER_NAME, equalTo("fooo")))
    }

    @Test
    fun `will cache response`() {
        val server = WireMockServer(WireMockConfiguration().dynamicPort())
        server.start()
        server.stubFor(get(urlPathEqualTo("/api/v1/user"))
                .willReturn(aResponse().withBody("""{ "user": { "email": "foo@example.com" } }""")))

        val baseUrl = server.url("/").toString()

        val config = ApplicationConfig()
        config.authHost = baseUrl
        config.disableAuth = false

        val authenticator = JwtAuthenticatorFilter(config, jacksonObjectMapper())
        for (i in 0..1) {
            val authenticate = authenticator.resolveCredentials("fooo").block() as GadgetPrincipal.AccountManagerPrincipal

            assertNotNull(authenticate)
            assertEquals("foo@example.com", authenticate.name)
            assertEquals("fooo", authenticate.token)
        }

        server.stop()
        server.verify(1, getRequestedFor(urlEqualTo("/api/v1/user"))
                .withHeader(HeaderConst.AUTH_HEADER_NAME, equalTo("fooo")))
    }
}