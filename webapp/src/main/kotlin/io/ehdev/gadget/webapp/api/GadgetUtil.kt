package io.ehdev.gadget.webapp.api

import io.ehdev.gadget.database.manager.api.RedirectManager
import io.ehdev.gadget.model.RedirectContainer
import io.ehdev.gadget.model.lazyLogger
import org.apache.commons.codec.net.URLCodec
import java.util.concurrent.CompletionStage

object GadgetUtil {
    private val log by lazyLogger()
    private val codec = URLCodec()

    public const val ALIAS = "name"

    fun findRequestRedirect(redirectManager: RedirectManager, requestPath: String): CompletionStage<RedirectContainer?> {
        val decidedPath = codec.decode(requestPath).split(" ")

        log.debug("Checking for path for {}", decidedPath)

        return redirectManager.getRedirect(decidedPath.first())
    }
}