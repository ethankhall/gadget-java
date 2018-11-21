package io.ehdev.gadget.model

import java.security.Principal
import java.util.UUID

sealed class GadgetPrincipal(val token: String) : Principal {

    class AccountManagerPrincipal(token: String, private val email: String) : GadgetPrincipal(token) {
        override fun getName(): String = email
    }

    class AnonymousManagerPrincipal : GadgetPrincipal("anonymous:${UUID.randomUUID()}") {
        override fun getName(): String = "Unknown"
    }
}