package io.ehdev.gadget.config

import dagger.Provides
import java.time.Clock

@dagger.Module
class ClockModule {

    @Provides
    fun provideClock(): Clock = Clock.systemUTC()
}
