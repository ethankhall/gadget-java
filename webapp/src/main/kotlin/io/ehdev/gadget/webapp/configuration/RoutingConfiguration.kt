package io.ehdev.gadget.webapp.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.ehdev.gadget.webapp.api.GadgetHtmlResource
import io.ehdev.gadget.webapp.api.GadgetJsonResource
import io.ehdev.gadget.webapp.api.RedirectResource
import io.ehdev.gadget.webapp.auth.JwtAuthenticatorFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.springframework.web.server.WebHandler
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Configuration
@Import(BusinessLogicConfiguration::class, WebFilterConfiguration::class)
open class RoutingConfiguration {

    @Bean
    open fun webHandler(router: RouterFunction<ServerResponse>): WebHandler = RouterFunctions.toWebHandler(router, HandlerStrategies.withDefaults())

    @Bean
    open fun mainServer(
        applicationConfig: ApplicationConfig,
        redirectResource: RedirectResource,
        gadgetJsonResource: GadgetJsonResource,
        gadgetHtmlResource: GadgetHtmlResource
    ): RouterFunction<ServerResponse> {

        return router {
            accept(MediaType.TEXT_HTML).nest {
                GET("/").invoke { it ->
                    val uri = it.uriBuilder().path("/gadget").build()
                    ServerResponse.temporaryRedirect(uri).build()
                }
                path("/gadget").nest {
                    GET("", gadgetHtmlResource::rootPage)
                    GET("/resource/{path}")
                    GET("/search", gadgetHtmlResource::searchFor)
                    GET("/new", gadgetHtmlResource::displayUpdatePage)
                    POST("/new", gadgetHtmlResource::updateRedirect)
                }

                GET("/{*path}", redirectResource::doRedirect)
            }
            accept(MediaType.APPLICATION_JSON).nest {
                path("/gadget").nest {
                    POST("/", gadgetJsonResource::createNewEndpoint)
                    GET("/resource/{path}", redirectResource::showRedirectJson)
                    GET("/search", gadgetJsonResource::searchRedirects)
                }
                GET("/{*path}", redirectResource::showRedirectJson)
            }
        }.filter { request, next ->
            if (request.path().startsWith("/gadget")) {
                request.principal()
                        .flatMap { next.handle(request) }
                        .switchIfEmpty(redirectToLogin(request, applicationConfig))
            } else {
                next.handle(request)
            }
        }
    }

    private fun redirectToLogin(request: ServerRequest, applicationConfig: ApplicationConfig): Mono<ServerResponse> {
        return Mono.defer {
            val redirectUri = UriComponentsBuilder.fromHttpUrl(applicationConfig.defaultLogin)
                    .queryParam("redirectTo", request.uri().toString())
                    .build()
            ServerResponse.temporaryRedirect(redirectUri.toUri()).build()
        }
    }

    @Bean
    open fun authFilter(applicationConfig: ApplicationConfig, objectMapper: ObjectMapper): JwtAuthenticatorFilter {
        return JwtAuthenticatorFilter(applicationConfig.authHost, objectMapper)
    }
}