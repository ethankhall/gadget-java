package io.ehdev.gadget.webapp.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.MalformedURLException

class RedirectDefinition @JsonCreator constructor(
    @JsonProperty("alias", required = true) val alias: String,
    @JsonProperty("destination", required = true) val destination: String
) {

    fun validateModel(): ModelValidationModel {
        val validation = ModelValidationModel()
        validateAliasDoesNotHaveGadget(validation)
        validateAliasInRange(validation)
        validateDestinationInRange(validation)
        validateDestinationValidUrl(validation)
        return validation
    }

    private fun validateAliasInRange(validation: ModelValidationModel) {
        val length = alias.trim().length
        if (length !in 2..128) {
            validation.addValidationFailure("alias must be between 2 and 128 char's long")
        }
    }

    private fun validateDestinationInRange(validation: ModelValidationModel) {
        val length = destination.trim().length
        if (length !in 5..4096) {
            validation.addValidationFailure("destination must be between 5 and 4096 char's long")
        }
    }

    private fun validateAliasDoesNotHaveGadget(validation: ModelValidationModel) {
        if (alias.startsWith("gadget", true)) {
            validation.addValidationFailure("alias cannot start with 'gadget'")
        }
    }

    private fun validateDestinationValidUrl(validation: ModelValidationModel) {
        try {
            if (!destination.contains("://")) {
                java.net.URI("http://$destination")
            } else {
                java.net.URI(destination)
            }
        } catch (e: MalformedURLException) {
            validation.addValidationFailure("destination is not a valid url")
        }
    }
}