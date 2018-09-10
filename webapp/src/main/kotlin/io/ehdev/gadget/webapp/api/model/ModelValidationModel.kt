package io.ehdev.gadget.webapp.api.model

import com.fasterxml.jackson.annotation.JsonProperty

class ModelValidationModel {
    @JsonProperty("errors")
    val validationError: MutableList<String> = mutableListOf()

    fun addValidationFailure(message: String) {
        validationError.add(message)
    }

    fun hasErrorMessages(): Boolean = validationError.isNotEmpty()
}