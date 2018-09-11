package io.ehdev.gadget.webapp.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.ViewResolverRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer

@Configuration
open class WebFilterConfiguration : WebFluxConfigurer {

    @Bean
    open fun freeMarkerConfig(applicationContext: ApplicationContext,
                              @Value("\${spring.template.path}") templatePath: String?): FreeMarkerConfigurer {
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