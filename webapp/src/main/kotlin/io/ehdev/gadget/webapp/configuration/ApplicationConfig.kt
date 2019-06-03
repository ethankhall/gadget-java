package io.ehdev.gadget.webapp.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("gadget")
class ApplicationConfig {
    var disableAuth: Boolean = false
    lateinit var authHost: String
    lateinit var defaultLogin: String
    lateinit var primaryUriBase: String
    var proxyDomains: Set<String> = emptySet()

    companion object {
        fun buildTestExample(): ApplicationConfig {
            val applicationConfig = ApplicationConfig()
            applicationConfig.authHost = "http://localhost:8080"
            applicationConfig.defaultLogin = "http://localhost:8080"
            applicationConfig.primaryUriBase = "http://localhost8081"
            applicationConfig.proxyDomains = emptySet()
            return applicationConfig
        }
    }
}
