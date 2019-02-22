package io.ehdev.gadget.webapp.configuration

import io.micrometer.core.instrument.MeterRegistry
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.SettingsTools
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

@Configuration
@Import(DataSourceAutoConfiguration::class)
open class DatabaseConfiguration {

    @Bean
    open fun dslContext(dataSource: TransactionAwareDataSourceProxy, metrics: MeterRegistry): DSLContext {
        val configuration = DefaultConfiguration()
        configuration.setDataSource(dataSource)
        configuration.setSQLDialect(SQLDialect.MYSQL)
        configuration.set(SettingsTools.defaultSettings().withRenderSchema(false).withRenderCatalog(false))
        configuration.set(JooqMetricsCollector(metrics, TimeUnit.SECONDS.toMillis(30)))
        return DSL.using(configuration)
    }

    @Bean
    open fun transactionAwareDataSourceProxy(dataSource: DataSource) = TransactionAwareDataSourceProxy(dataSource)
}