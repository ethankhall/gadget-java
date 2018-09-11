package io.ehdev.gadget.webapp.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("gadget")
class ApplicationConfig {
    lateinit var authHost: String
    lateinit var defaultLogin: String
}
