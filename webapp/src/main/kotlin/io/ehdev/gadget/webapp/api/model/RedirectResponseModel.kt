package io.ehdev.gadget.webapp.api.model

data class RedirectResponseModel(val source: String, val destination: String)

data class PrivilegedRedirectResponseModel(val source: String, val destination: String, val createdBy: String)