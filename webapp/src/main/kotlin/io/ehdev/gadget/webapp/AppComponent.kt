package io.ehdev.gadget.webapp

import dagger.Component
import io.ehdev.gadget.auth.UserFilter
import io.ehdev.gadget.auth.module.AuthModule
import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.database.manager.module.DatabaseManagerModule

@Component(modules = [DatabaseManagerModule::class, AuthModule::class])
interface AppComponent {
    fun providesRedirectManager(): RedirectManager

    fun providesUserFilter(): UserFilter
}