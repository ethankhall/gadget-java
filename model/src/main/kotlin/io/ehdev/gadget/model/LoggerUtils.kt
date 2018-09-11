package io.ehdev.gadget.model

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <R : Any> R.lazyLogger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this.javaClass) }
}
