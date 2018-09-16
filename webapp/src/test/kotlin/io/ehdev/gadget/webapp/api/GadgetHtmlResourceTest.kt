package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.RedirectContainer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import java.util.concurrent.CompletableFuture

internal class GadgetHtmlResourceTest {

    @BeforeEach
    fun setup() {
        Mockito.reset(redirectManager)
    }

    @Test
    fun `normal get will work`() {
        val redirect = CompletableFuture.supplyAsync { RedirectContainer("foo", listOf("1"), "http://bar.com/{1}") }
        Mockito.`when`(redirectManager.getRedirect("foo")).thenReturn(redirect)
        val request = Mockito.mock(ServerRequest::class.java)
        Mockito.`when`(request.pathVariable("path")).thenReturn("foo")

        val response = redirectResource.doRedirect(request).block()!!
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT)
        Assertions.assertThat(response.headers().location!!.toString()).isEqualTo("http://bar.com/")

        Mockito.verify(redirectManager, times(1)).getRedirect("foo")
    }

    @Test
    fun `parameter get will work`() {
        val redirect = CompletableFuture.supplyAsync { RedirectContainer("foo", listOf("1"), "http://bar.com/{\$1}") }
        Mockito.`when`(redirectManager.getRedirect("foo")).thenReturn(redirect)
        val request = Mockito.mock(ServerRequest::class.java)
        Mockito.`when`(request.pathVariable("path")).thenReturn("foo blarg")


        val response = redirectResource.doRedirect(request).block()!!
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT)
        Assertions.assertThat(response.headers().location!!.toString()).isEqualTo("http://bar.com/blarg")

        Mockito.verify(redirectManager, times(1)).getRedirect("foo")

    }

    @Test
    fun `parameter and extra get will work`() {
        val redirect = CompletableFuture.supplyAsync { RedirectContainer("foo", listOf("1"), "http://bar.com/{\$1}") }
        Mockito.`when`(redirectManager.getRedirect("foo")).thenReturn(redirect)
        val request = Mockito.mock(ServerRequest::class.java)
        Mockito.`when`(request.pathVariable("path")).thenReturn("foo blah flig")


        val response = redirectResource.doRedirect(request).block()!!
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT)
        Assertions.assertThat(response.headers().location!!.toString()).isEqualTo("http://bar.com/blah%20flig")

        Mockito.verify(redirectManager, times(1)).getRedirect("foo")
    }


    @Test
    fun `subresource get will work`() {
        val redirect = CompletableFuture.supplyAsync { RedirectContainer("foo/bar", listOf(), "http://bar.com/") }
        Mockito.`when`(redirectManager.getRedirect("foo/bar")).thenReturn(redirect)
        Mockito.`when`(redirectManager.getRedirect("foo")).thenReturn(CompletableFuture.supplyAsync { null })
        val request = Mockito.mock(ServerRequest::class.java)
        Mockito.`when`(request.pathVariable("path")).thenReturn("foo/bar")


        val response = redirectResource.doRedirect(request).block()!!
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT)
        Assertions.assertThat(response.headers().location!!.toString()).isEqualTo("http://bar.com/")

        Mockito.verify(redirectManager, times(1)).getRedirect("foo/bar")
        Mockito.verifyNoMoreInteractions(redirectManager)
    }

    @Test
    fun `subresource with variable and extra get will work`() {
        val redirect = CompletableFuture.supplyAsync { RedirectContainer("foo/bar", listOf("1"), "http://bar.com/{\$1}") }
        Mockito.`when`(redirectManager.getRedirect("foo/bar")).thenReturn(redirect)
        val request = Mockito.mock(ServerRequest::class.java)
        Mockito.`when`(request.pathVariable("path")).thenReturn("foo/bar blig flarg")


        val response = redirectResource.doRedirect(request).block()!!
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.TEMPORARY_REDIRECT)
        Assertions.assertThat(response.headers().location!!.toString()).isEqualTo("http://bar.com/blig%20flarg")

        Mockito.verify(redirectManager, times(1)).getRedirect("foo/bar")
        Mockito.verifyNoMoreInteractions(redirectManager)

    }

    companion object {
        private val redirectManager: RedirectManager = Mockito.mock(RedirectManager::class.java)
        private val redirectResource: RedirectResource = DefaultRedirectResource(redirectManager)
    }
}