package io.ehdev.gadget.webapp.api.model

import io.ehdev.gadget.model.RedirectContainer

data class SearchResponseModel(val count: Int, val results: List<SearchResponseModelResult>)

data class SearchResponseModelResult(val source: String, val destination: String, val createdBy: String) {
    constructor(source: RedirectContainer) : this(source.aliasRoot, source.redirect, source.createdBy.split("@").first())

    fun getUserName() = createdBy.split("@").first()
}