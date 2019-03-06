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
            jsonResource: GadgetJsonResource,
            htmlResource: GadgetHtmlResource
    ): RouterFunction<ServerResponse> {
        val managementPort = environment.getProperty("management.server.port", "-2").toInt()

        return router {
            GET("/favicon.ico").invoke {
                return@invoke ServerResponse.notFound().build()
            }

            RequestPredicate { applicationConfig.proxyDomains.contains(it.uri().host) }.nest {
                GET("/{*path}", redirectResource::doRedirect)

                GET("/") {
                    val uri = UriComponentsBuilder.fromUriString(applicationConfig.primaryUriBase)
                            .path("/gadget")
                            .build()
                            .toUri()
                    temporaryRedirect(uri).build()
                }
            }

            RequestPredicate { it.uri().port != managementPort }.nest {
                accept(MediaType.TEXT_HTML).nest {
                    GET("/").invoke {
                        val uri = UriComponentsBuilder.fromUriString(applicationConfig.primaryUriBase)
                                .path("/gadget")
                                .build()
                                .toUri()
                        return@invoke ServerResponse.temporaryRedirect(uri).build()
                    }
                    path("/gadget").nest {
                        GET("", htmlResource::getRootPage)
                        GET("/edit", htmlResource::getEditPathPage)
                        GET("/search", htmlResource::getSearchPage)
                        GET("/new", htmlResource::getNewPage)
                        GET("/delete", htmlResource::getDeletePathPage)
                    }

                    GET("/{*path}", redirectResource::doRedirect)
                }
                accept(MediaType.APPLICATION_JSON).nest {
                    path("/gadget").nest {
                        POST("/resource", jsonResource::createNewEndpoint)
                        GET("/resource/{*path}") { jsonResource.showRedirectJson(it, true) }
                        DELETE("/resource/{*path}", jsonResource::deleteRedirect)
                        PUT("/resource/{*path}", jsonResource::updateRedirect)
                        GET("/search", jsonResource::searchRedirects)
                    }
                    GET("/{*path}") { jsonResource.showRedirectJson(it, false) }
                }
            }
        }
    }
}