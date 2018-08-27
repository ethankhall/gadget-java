package io.ehdev.gadget.config

import dagger.Provides
import org.jooq.DSLContext

@dagger.Module
class DatabaseModule(private val dslContext: DSLContext) {

    @Provides
    fun providesDSLContext(): DSLContext = dslContext
}