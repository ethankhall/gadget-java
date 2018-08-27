package io.ehdev.gadget.auth

import io.dropwizard.auth.AuthFilter
import io.ehdev.gadget.model.AccountManagerPrincipal
import javax.ws.rs.WebApplicationException
import javax.ws.rs.container.ContainerRequestContext

class UserFilter: AuthFilter<JwtCredentials, AccountManagerPrincipal>() {
    override fun filter(requestContext: ContainerRequestContext) {
        val credentials = listOf(getCredentialsFromCookie(requestContext), getCredentialsFromHeader(requestContext))
        val jwtCreds = credentials.filterNotNull().firstOrNull()
        if (!authenticate(requestContext, jwtCreds, "DIGEST_AUTH")) {
            throw WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm))
        }
    }

    private fun getCredentialsFromHeader(requestContext: ContainerRequestContext): JwtCredentials? {
        val value = requestContext.headers.getFirst(HEADER_NAME)
        return makeToken(value)
    }

    private fun getCredentialsFromCookie(requestContext: ContainerRequestContext): JwtCredentials? {
        val value = requestContext.cookies[COOKIE_NAME]?.value
        return makeToken(value)
    }

    private fun makeToken(value: String?) : JwtCredentials? {
        return if (value != null) JwtCredentials(value) else null
    }

    class Builder : AuthFilter.AuthFilterBuilder<JwtCredentials, AccountManagerPrincipal, UserFilter>() {
        override fun newInstance(): UserFilter {
            return UserFilter()
        }
    }

    companion object {
        const val HEADER_NAME = "X-AUTH-TOKEN"
        const val COOKIE_NAME = "account-manager"
    }
}