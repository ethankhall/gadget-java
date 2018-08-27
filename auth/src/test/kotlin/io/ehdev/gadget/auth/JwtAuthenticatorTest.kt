package io.ehdev.gadget.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit


internal class JwtAuthenticatorTest {

    @Test
    fun `will make request to account-manager`() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody("""{ "email": "foo@example.com" }"""))

        server.start()
        val baseUrl = server.url("/").toString()

        val authenticator = JwtAuthenticator(baseUrl, jacksonObjectMapper())
        val authenticate = authenticator.authenticate(JwtCredentials("fooo"))

        assertTrue(authenticate.isPresent)
        assertEquals("foo@example.com", authenticate.get().name)
        assertEquals("fooo", authenticate.get().token)

        val request = server.takeRequest()
        assertEquals("fooo", request.headers[UserFilter.HEADER_NAME])
    }

    @Test
    fun `on error, will retry`() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody("""{ "email": "foo@example.com" }""").setHeadersDelay(10, TimeUnit.SECONDS))
        server.enqueue(MockResponse().setBody("""{ "email": "foo@example.com" }"""))

        server.start()
        val baseUrl = server.url("/").toString()

        val authenticator = JwtAuthenticator(baseUrl, jacksonObjectMapper())
        assertTrue(authenticator.authenticate(JwtCredentials("fooo")).isPresent)

        val request = server.takeRequest()
        assertEquals(2, server.requestCount)
        assertEquals("fooo", request.headers[UserFilter.HEADER_NAME])
    }

    @Test
    fun `will cache response`() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody("""{ "email": "foo@example.com" }"""))

        server.start()
        val baseUrl = server.url("/").toString()

        val authenticator = JwtAuthenticator(baseUrl, jacksonObjectMapper())
        for (i in 0..1) {
            val authenticate = authenticator.authenticate(JwtCredentials("fooo"))

            assertTrue(authenticate.isPresent)
            assertEquals("foo@example.com", authenticate.get().name)
            assertEquals("fooo", authenticate.get().token)
        }

        assertEquals(1, server.requestCount)
        val request = server.takeRequest()
        assertEquals("fooo", request.headers[UserFilter.HEADER_NAME])
    }
}