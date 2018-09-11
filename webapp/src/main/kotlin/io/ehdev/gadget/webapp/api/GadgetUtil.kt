package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.lazyLogger
import java.net.URLDecoder
import java.nio.charset.Charset

object GadgetUtil {
    private val log by lazyLogger()

    fun findRequestRedirect(redirectManager: RedirectManager, requestPath: String): String? {
        val decidedPath = URLDecoder.decode(requestPath, Charset.defaultCharset()).split(" ")

        log.debug("Checking for path for {}", decidedPath)

        val redirectContainer = redirectManager.getRedirect(decidedPath.first())
        return redirectContainer
                .thenApply { it?.buildRedirect(requestPath) }
                .toCompletableFuture()
                .get()

    }
}