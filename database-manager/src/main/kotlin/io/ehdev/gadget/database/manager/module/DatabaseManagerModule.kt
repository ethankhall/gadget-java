package io.ehdev.gadget.database.manager.module

import dagger.Provides
import io.ehdev.gadget.config.ClockModule
import io.ehdev.gadget.config.DatabaseModule
import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.database.manager.impl.DefaultRedirectManager
import org.jooq.DSLContext
import java.time.Clock

@dagger.Module(includes = [DatabaseModule::class, ClockModule::class])
class DatabaseManagerModule {

    @Provides
    fun redirectManager(dslContext: DSLContext, clock: Clock): RedirectManager {
        return DefaultRedirectManager(dslContext, clock)
    }
}