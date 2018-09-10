package io.ehdev.gadget.webapp

import io.ehdev.gadget.webapp.configuration.RoutingConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.server.adapter.WebHttpHandlerBuilder

@Import(
        DataSourceAutoConfiguration::class,
        RoutingConfiguration::class
)
@Configuration
@EnableWebFlux
open class MainConfiguration {

    @Bean
    open fun httpHandler(context: ApplicationContext): HttpHandler {
        return WebHttpHandlerBuilder.applicationContext(context).build()
    }

    @Bean
    open fun nettyReactiveWebServerFactory() = NettyReactiveWebServerFactory()
}