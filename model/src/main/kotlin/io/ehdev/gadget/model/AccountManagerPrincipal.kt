package io.ehdev.gadget.model

import java.security.Principal

class AccountManagerPrincipal(val token: String, private val email: String) : Principal {
    override fun getName(): String = email
}