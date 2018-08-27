package io.ehdev.gadget.webapp.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.validation.ValidationMethod
import java.net.MalformedURLException

class NewRedirect @JsonCreator constructor(
        @JsonProperty("alias", required = true) val alias: String,
        @JsonProperty("variables", required = false) val variables: List<String>?,
        @JsonProperty("destination", required = true) val destination: String) {

    @ValidationMethod(message = "alias must be between 2 and 128 char's long")
    fun isAliasInRange() : Boolean {
        val length = alias.trim().length
        return length in 2..128
    }

    @ValidationMethod(message = "destination must be between 5 and 4096 char's long")
    fun isDestinationInRange() : Boolean {
        val length = destination.trim().length
        return length in 5..4096
    }

    @ValidationMethod(message = "alias cannot start with 'gadget'")
    fun isAliasDoesNotHaveGadget(): Boolean {
        return !alias.startsWith("gadget", true)
    }

    @ValidationMethod(message = "to many variables, joined list can only be 128 characters")
    fun isVariableListToLong(): Boolean {
        return (variables ?: emptyList()).joinToString(",").length <= 128
    }

    @ValidationMethod(message = "destination is not a valid url")
    fun isDestinationValidUrl(): Boolean {
        return try {
            if (!destination.contains("://")) {
                java.net.URI("http://$destination")
            } else {
                java.net.URI(destination)
            }
            true
        } catch (e: MalformedURLException) {
            false
        }
    }

    @ValidationMethod(message = "destination has unknown variables")
    fun isDestinationInCorrectFormat(): Boolean {
        val matches = variableMatcher.findAll(destination).map { it.value.drop(1) }
        for (match in matches) {
            if (match !in (variables ?: emptyList())) {
                return false
            }
        }

        return true
    }


    companion object {
        private val variableMatcher = Regex("\$[a-zA-Z0-9_-]+")
    }
}