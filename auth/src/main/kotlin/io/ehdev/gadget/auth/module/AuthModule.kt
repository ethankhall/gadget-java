package io.ehdev.gadget.auth.module

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Provides
import io.ehdev.gadget.auth.JwtAuthenticator
import io.ehdev.gadget.auth.UserFilter

@dagger.Module()
class AuthModule(private val accountManagerBaseUrl: String, private val om: ObjectMapper) {

    @Provides
    fun userFilter(): UserFilter {
        return UserFilter.Builder()
                .setAuthenticator(JwtAuthenticator(accountManagerBaseUrl, om))
                .buildAuthFilter()
    }
}