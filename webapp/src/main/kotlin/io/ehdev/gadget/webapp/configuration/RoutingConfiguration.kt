package io.ehdev.gadget.webapp.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.webapp.api.GadgetResource
import io.ehdev.gadget.webapp.api.RedirectResource
import io.ehdev.gadget.webapp.auth.JwtAuthenticatorFilter
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.WebHandler

open class RoutingConfiguration {
    @Bean
    open fun webHandler(router: RouterFunction<ServerResponse>): WebHandler = RouterFunctions.toWebHandler(router, HandlerStrategies.withDefaults())

    @Bean
    open fun redirectResource(redirectManager: RedirectManager): RedirectResource = RedirectResource(redirectManager)

    @Bean
    open fun mainServer(redirectResource: RedirectResource,
                        gadgetResource: GadgetResource): RouterFunction<ServerResponse> {

        return router {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/{path: (?!gadget).+}", redirectResource::showRedirectJson)
                path("/gadget").nest {
                    POST("/", gadgetResource::createNewEndpoint)
                    GET("/{path}", redirectResource::showRedirectJson)
                    GET("/search/{path}", gadgetResource::searchRedirects)
                }
            }
            accept(MediaType.TEXT_HTML).nest {
                GET("/{path: (?!gadget).+}", redirectResource::doRedirect)
                GET("/").invoke { it ->
                    val uri = it.uriBuilder().path("/gadget").build()
                    ServerResponse.temporaryRedirect(uri).build()
                }
                GET("/gadget")
                GET("/gadget/{path}")
            }
        }
    }

    @Bean
    open fun authFilter(objectMapper: ObjectMapper) = JwtAuthenticatorFilter("", objectMapper)
}