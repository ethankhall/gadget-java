package io.ehdev.gadget.webapp.configuration

import io.ehdev.gadget.webapp.api.GadgetHtmlResource
import io.ehdev.gadget.webapp.api.GadgetJsonResource
import io.ehdev.gadget.webapp.api.RedirectResource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.springframework.web.util.UriComponentsBuilder

@Configuration
@Import(BusinessLogicConfiguration::class, WebFilterConfiguration::class)
open class RoutingConfiguration {

    @Bean("mainRouter")
    open fun mainServer(
        environment: Environment,
        applicationConfig: ApplicationConfig,
        redirectResource: RedirectResource,
        gadgetJsonResource: GadgetJsonResource,
        gadgetHtmlResource: GadgetHtmlResource
    ): RouterFunction<ServerResponse> {
        val managementPort = environment.getProperty("management.server.port", "-2").toInt()

        return router {
            RequestPredicate { it.uri().port != managementPort }.nest {
                accept(MediaType.TEXT_HTML).nest {
                    GET("/").invoke { it ->
                        val uri = it.uriBuilder().path("/gadget")
                                .scheme(it.findScheme())
                                .build()
                        return@invoke ServerResponse.temporaryRedirect(uri).build()
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
                        GET("/resource/{*path}", redirectResource::showRedirectJson)
                        GET("/search", gadgetJsonResource::searchRedirects)
                    }
                    GET("/{*path}", redirectResource::showRedirectJson)
                }
            }
        }
    }
}