package io.ehdev.gadget.webapp.api

import org.springframework.web.reactive.function.server.ServerRequest

data class SearchParameters(val aliasName: String, val page: Int, val size: Int) {
    constructor(request: ServerRequest) : this(
            request.queryParam(GadgetUtil.ALIAS).orElse(""),
            request.queryParam("page").orElse("0").toInt(),
            request.queryParam("size").orElse("20").toInt())

    fun offset(): Int = page * size
}