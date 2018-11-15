package io.ehdev.gadget.webapp.configuration

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
@Import(DataSourceAutoConfiguration::class)
open class DatabaseConfiguration {

    @Bean
    open fun dslContext(dataSource: TransactionAwareDataSourceProxy): DSLContext {
        return DSL.using(dataSource, SQLDialect.MYSQL)
    }

    @Bean
    open fun transactionAwareDataSourceProxy(dataSource: DataSource) = TransactionAwareDataSourceProxy(dataSource)
}