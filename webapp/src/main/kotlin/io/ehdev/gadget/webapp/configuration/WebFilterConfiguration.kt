package io.ehdev.gadget.webapp.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.ehdev.gadget.webapp.auth.JwtAuthenticatorFilter
import io.ehdev.gadget.webapp.auth.LoggedInLoginFilter
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.config.ViewResolverRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer

@Configuration
open class WebFilterConfiguration : WebFluxConfigurer {

    @Bean
    open fun authFilter(applicationConfig: ApplicationConfig, objectMapper: ObjectMapper): JwtAuthenticatorFilter {
        return JwtAuthenticatorFilter(applicationConfig.authHost, objectMapper)
    }

    @Bean
    open fun loginFilter(environment: Environment, applicationConfig: ApplicationConfig) = LoggedInLoginFilter(environment, applicationConfig)

    @Bean
    open fun freeMarkerConfig(applicationContext: ApplicationContext, environment: Environment): FreeMarkerConfigurer {
        val templatePath = environment.getProperty("spring.template.path")
        val configurer = FreeMarkerConfigurer()
        configurer.setPreferFileSystemAccess(templatePath != null)
        configurer.setTemplateLoaderPath(templatePath ?: "classpath:/templates/")
        configurer.setResourceLoader(applicationContext)
        return configurer
    }

    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        registry.freeMarker()
    }
}