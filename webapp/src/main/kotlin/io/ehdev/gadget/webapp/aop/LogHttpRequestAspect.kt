package io.ehdev.gadget.webapp.aop

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.Supplier
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.web.reactive.function.server.ServerRequest

@Aspect
class LogHttpRequestAspect {

    private val log: Logger = LogManager.getLogger(LogHttpRequestAspect::class.java)

    @Before("execution(reactor.core.publisher.Mono io.ehdev.gadget..*.*(org.springframework.web.reactive.function.server.ServerRequest))")
    fun logExecutionTime(joinPoint: JoinPoint) {
        val method = Supplier {
            val sig = joinPoint.signature
            "${sig.declaringType.name}#${sig.name}"
        }

        val firstArg = joinPoint.args[0]
        val request = when (firstArg) {
            is ServerRequest -> firstArg
            else -> {
                log.error("{} did not have ServerRequest as the first argument", method)
                return
            }
        }

        log.info("A request was made to `{}` and was handled by {}.", Supplier { request.uri() }, method)
    }
}