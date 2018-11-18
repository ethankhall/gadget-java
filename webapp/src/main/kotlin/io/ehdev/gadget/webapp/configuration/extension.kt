package io.ehdev.gadget.webapp.configuration

import org.springframework.web.reactive.function.server.ServerRequest

fun ServerRequest.findScheme(): String {
    return this.headers().header("X-Forwarded-Proto").first() ?: this.uri().scheme
}