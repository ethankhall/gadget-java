package io.ehdev.gadget.webapp

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.util.HierarchicalNameMapper
import io.micrometer.graphite.GraphiteConfig
import io.micrometer.graphite.GraphiteMeterRegistry
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.core.env.Environment

@Configuration
@SpringBootApplication
@EnableAspectJAutoProxy
open class MainConfiguration {

    @Bean
    @ConditionalOnBean(GraphiteConfig::class)
    open fun graphiteMeterRegistry(config: GraphiteConfig, clock: Clock, env: Environment): GraphiteMeterRegistry {
        val prefix = env.getProperty("metric.prefix", "gadget")

        return GraphiteMeterRegistry(
                config, clock,
                HierarchicalNameMapper { id, convention -> "$prefix." + HierarchicalNameMapper.DEFAULT.toHierarchicalName(id, convention) })
    }

    @Bean
    open fun metricsCommonTags(): MeterRegistryCustomizer<MeterRegistry> {
        return MeterRegistryCustomizer { registry -> registry.config().commonTags("application", "gadget") }
    }
}