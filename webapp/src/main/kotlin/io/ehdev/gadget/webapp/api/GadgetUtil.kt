package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.lazyLogger
import org.apache.commons.codec.net.URLCodec

object GadgetUtil {
    private val log by lazyLogger()
    private val codec = URLCodec()

    fun findRequestRedirect(redirectManager: RedirectManager, requestPath: String): String? {
        val decidedPath = codec.decode(requestPath).split(" ")

        log.debug("Checking for path for {}", decidedPath)

        val redirectContainer = redirectManager.getRedirect(decidedPath.first())
        return redirectContainer
                .thenApply { it?.buildRedirect(requestPath) }
                .toCompletableFuture()
                .get()
    }
}