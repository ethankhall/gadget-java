package io.ehdev.gadget.webapp.configuration

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.database.manager.impl.DefaultRedirectManager
import io.ehdev.gadget.webapp.aop.LogHttpRequestAspect
import io.ehdev.gadget.webapp.api.DefaultGadgetHtmlResource
import io.ehdev.gadget.webapp.api.DefaultGadgetJsonResource
import io.ehdev.gadget.webapp.api.DefaultRedirectResource
import io.ehdev.gadget.webapp.api.GadgetHtmlResource
import io.ehdev.gadget.webapp.api.GadgetJsonResource
import io.ehdev.gadget.webapp.api.RedirectResource
import org.jooq.DSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.time.Clock

@Configuration
@Import(DatabaseConfiguration::class)
open class BusinessLogicConfiguration {

    @Bean
    open fun clock(): Clock = Clock.systemUTC()

    @Bean
    open fun redirectManager(dslContext: DSLContext, clock: Clock): RedirectManager = DefaultRedirectManager(dslContext, clock)

    @Bean
    open fun redirectResource(redirectManager: RedirectManager, applicationConfig: ApplicationConfig): RedirectResource {
        return DefaultRedirectResource(redirectManager, applicationConfig)
    }

    @Bean
    open fun gadgetJsonResource(redirectManager: RedirectManager): GadgetJsonResource = DefaultGadgetJsonResource(redirectManager)

    @Bean
    open fun gadgetHtmlResource(redirectManager: RedirectManager): GadgetHtmlResource {
        return DefaultGadgetHtmlResource(redirectManager)
    }

    @Bean
    open fun requestRequestAspect() = LogHttpRequestAspect()
}