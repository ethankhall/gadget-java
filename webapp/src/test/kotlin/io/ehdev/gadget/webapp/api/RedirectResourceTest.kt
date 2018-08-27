package io.ehdev.gadget.webapp.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.dropwizard.testing.junit5.ResourceExtension
import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.RedirectContainer
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@ExtendWith(DropwizardExtensionsSupport::class)
internal class RedirectResourceTest {

    @BeforeEach
    fun setup() {
        Mockito.reset(redirectManager)
    }

    @Test
    fun `normal get will work`() {
        Mockito.`when`(redirectManager.getRedirect("foo"))
                .thenReturn(RedirectContainer("foo", listOf("1"), "http://bar.com/{1}"))

        val response = resources.target("/foo").request(MediaType.APPLICATION_JSON_TYPE).get()
        Assertions.assertThat(response.statusInfo).isEqualTo(Response.Status.OK)

        val responseBody = response.readEntity(Map::class.java)
        assertThat("foo").isEqualTo(responseBody["source"])
        assertThat("http://bar.com/").isEqualTo(responseBody["destination"])

        Mockito.verify(redirectManager, times(1)).getRedirect("foo")
    }

    @Test
    fun `parameter get will work`() {
        Mockito.`when`(redirectManager.getRedirect("foo"))
                .thenReturn(RedirectContainer("foo", listOf("1"), "http://bar.com/{\$1}"))

        val response = resources.target("/foo blah").request(MediaType.APPLICATION_JSON_TYPE).get()
        Assertions.assertThat(response.statusInfo).isEqualTo(Response.Status.OK)

        val responseBody = response.readEntity(Map::class.java)
        assertThat("foo blah").isEqualTo(responseBody["source"])
        assertThat("http://bar.com/blah").isEqualTo(responseBody["destination"])

        Mockito.verify(redirectManager, times(1)).getRedirect("foo")
    }

    @Test
    fun `parameter and extra get will work`() {
        Mockito.`when`(redirectManager.getRedirect("foo"))
                .thenReturn(RedirectContainer("foo", listOf("1"), "http://bar.com/{\$1}"))

        val response = resources.target("/foo blah flig").request(MediaType.APPLICATION_JSON_TYPE).get()
        Assertions.assertThat(response.statusInfo).isEqualTo(Response.Status.OK)

        val responseBody = response.readEntity(Map::class.java)
        assertThat("foo blah flig").isEqualTo(responseBody["source"])
        assertThat("http://bar.com/blah flig").isEqualTo(responseBody["destination"])

        Mockito.verify(redirectManager, times(1)).getRedirect("foo")
    }

    @Test
    fun `subresource get will work`() {
        Mockito.`when`(redirectManager.getRedirect("foo/bar"))
                .thenReturn(RedirectContainer("foo/bar", listOf(), "http://bar.com/"))

        val response1 = resources.target("/foo/bar").request(MediaType.APPLICATION_JSON_TYPE).get()
        Assertions.assertThat(response1.statusInfo).isEqualTo(Response.Status.OK)

        val response2 = resources.target("/foo").request(MediaType.APPLICATION_JSON_TYPE).get()
        Assertions.assertThat(response2.statusInfo).isEqualTo(Response.Status.NOT_FOUND)

        val responseBody = response1.readEntity(Map::class.java)
        assertThat("foo/bar").isEqualTo(responseBody["source"])
        assertThat("http://bar.com/").isEqualTo(responseBody["destination"])

        Mockito.verify(redirectManager, times(1)).getRedirect("foo/bar")
        Mockito.verify(redirectManager, times(1)).getRedirect("foo")
        Mockito.verifyNoMoreInteractions(redirectManager)
    }

    @Test
    fun `subresource with variable and extra get will work`() {
        Mockito.`when`(redirectManager.getRedirect("foo/bar"))
                .thenReturn(RedirectContainer("foo/bar", listOf("1"), "http://bar.com/{\$1}"))

        val response1 = resources.target("/foo/bar blig flarg").request(MediaType.APPLICATION_JSON_TYPE).get()
        Assertions.assertThat(response1.statusInfo).isEqualTo(Response.Status.OK)

        val responseBody = response1.readEntity(Map::class.java)
        assertThat("foo/bar blig flarg").isEqualTo(responseBody["source"])
        assertThat("http://bar.com/blig flarg").isEqualTo(responseBody["destination"])

        Mockito.verify(redirectManager, times(1)).getRedirect("foo/bar")
        Mockito.verifyNoMoreInteractions(redirectManager)
    }

    companion object {
        private val redirectManager: RedirectManager = Mockito.mock(RedirectManager::class.java)
        private val om = jacksonObjectMapper()

        val resources = ResourceExtension.builder()
                .addResource(RedirectResource(redirectManager))
                .setMapper(om)
                .build()!!
    }
}