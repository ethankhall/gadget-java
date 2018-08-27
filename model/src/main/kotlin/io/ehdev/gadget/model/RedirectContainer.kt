package io.ehdev.gadget.model

import java.net.URLDecoder
import java.nio.charset.Charset

class RedirectContainer(private val aliasRoot: String, private val variableNames: List<String>, private val redirect: String) {

    fun buildRedirect(path: String): String {
        val (variables, extra) = buildVariableMap(path)

        val redirectComponents = redirect.split("{").map { it.trim('}') }
        var mergedString = redirectComponents.take(variables.size + 1).joinToString("")

        variables.forEach { key, value ->
            mergedString = mergedString.replace("\$$key", value)
        }

        mergedString = listOf(mergedString, *extra.toTypedArray()).joinToString(" ")

        return mergedString
    }

    private fun buildVariableMap(path: String): Pair<Map<String, String>, List<String>> {
        val decoded = URLDecoder.decode(path, Charset.defaultCharset()).trim()
        val variables = decoded.split(" ").drop(1)

        val variableMap = mutableMapOf<String, String>()

        val numberOfVariables = Math.min(variableNames.size, variables.size)
        if (numberOfVariables == 0) {
            return Pair(emptyMap(), variables)
        }

        for (i in 0..(numberOfVariables - 1)) {
            val name = variables[i]
            val variableName = variableNames[i]

            variableMap[variableName] = name
        }
        return Pair(variableMap, variables.drop(numberOfVariables))
    }
}