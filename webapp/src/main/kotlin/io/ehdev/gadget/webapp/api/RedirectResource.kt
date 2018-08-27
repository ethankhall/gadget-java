package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.config.getLogger
import io.ehdev.gadget.database.manager.api.RedirectManager
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.Charset
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/{path: (?!gadget).+}")
class RedirectResource(private val redirectManager: RedirectManager) {

    private val log by getLogger()

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun doRedirect(@PathParam("path") requestPath: String): Response {
        val redirectUrl = GadgetUtil.findRequestRedirect(redirectManager, requestPath)
        redirectUrl ?: return Response.status(Response.Status.NOT_FOUND).build()
        return Response.temporaryRedirect(URI.create(redirectUrl)).build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun showRedirectJson(@PathParam("path") requestPath: String): Response {
        val redirectUrl = GadgetUtil.findRequestRedirect(redirectManager, requestPath)
        redirectUrl ?: return Response.status(Response.Status.NOT_FOUND).build()
        return Response.ok(mapOf("source" to requestPath, "destination" to redirectUrl)).build()
    }
}