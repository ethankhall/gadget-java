package io.ehdev.gadget.database.manager.api

import io.ehdev.gadget.model.RedirectContainer

interface RedirectManager {

    fun setRedirect(aliasPath: String, variables: List<String>, destination: String, userName: String)

    fun getRedirect(aliasPath: String): RedirectContainer?

    fun removeRedirect(aliasPath: String): Boolean

    fun searchFor(rootPath: String): List<RedirectContainer>
}