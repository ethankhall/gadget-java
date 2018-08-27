package io.ehdev.gadget.webapp.api

import io.dropwizard.auth.Auth
import io.ehdev.gadget.config.getLogger
import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.AccountManagerPrincipal
import io.ehdev.gadget.webapp.api.model.NewRedirect
import java.net.URLDecoder
import java.nio.charset.Charset
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/gadget")
class GadgetResource(private val redirectManager: RedirectManager) {

    private val log by getLogger()

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createNewEndpoint(@Auth user: AccountManagerPrincipal,
                          @Context uriInfo: UriInfo,
                          @Valid redirectDefinition: NewRedirect): Response {
        redirectManager.setRedirect(redirectDefinition.alias,
                redirectDefinition.variables ?: emptyList(),
                redirectDefinition.destination, user.name)

        val redirectPath = uriInfo.baseUriBuilder.path("/gadget/redirect/${redirectDefinition.alias}").build()
        return Response.temporaryRedirect(redirectPath).build()
    }

    @GET
    @Path("/redirect/{path: (?!gadget).+}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getRedirect(@PathParam("path") requestPath: String): Response {
        val redirectUrl = GadgetUtil.findRequestRedirect(redirectManager, requestPath)
        redirectUrl ?: return Response.status(Response.Status.NOT_FOUND).build()
        return Response.ok(mapOf("source" to requestPath, "destination" to redirectUrl)).build()
    }

    @GET
    @Path("/search/{path: (?!gadget).+}")
    @Produces(MediaType.APPLICATION_JSON)
    fun searchForRedirect(@PathParam("path") requestPath: String): Response {
        val rootPath = URLDecoder.decode(requestPath, Charset.defaultCharset()).split(" ").first()
        return redirectManager.searchFor(rootPath)
    }

}