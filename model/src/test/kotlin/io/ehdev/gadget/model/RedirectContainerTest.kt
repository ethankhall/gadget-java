package io.ehdev.gadget.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RedirectContainerTest {

    @Test
    fun `without variable`() {
        val redirect = RedirectContainer("/foo", emptyList(), "http://bar", "")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar%20flig", redirect.buildRedirect("/foo%20flig"))
    }

    @Test
    fun `with variable`() {
        val redirect = RedirectContainer("/foo", listOf("1"), "http://bar{/\$1/foo}", "")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar/flig/foo", redirect.buildRedirect("/foo%20flig"))
    }

    @Test
    fun `with variables`() {
        val redirect = RedirectContainer("/foo", listOf("1", "hi"), "http://bar{/\$1/foo{/\$hi}}", "")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar/flig/foo", redirect.buildRedirect("/foo%20flig"))
        assertEquals("http://bar/flig/foo/blarg", redirect.buildRedirect("/foo%20flig%20blarg"))
    }

    @Test
    fun `with extra variables`() {
        val redirect = RedirectContainer("/foo", listOf("1"), "http://bar{/\$1/foo}", "")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar/flig/foo", redirect.buildRedirect("/foo%20flig"))
        assertEquals("http://bar/flig/foo%20blarg", redirect.buildRedirect("/foo%20flig%20blarg"))
    }

    @Test
    fun `will parse out variable`() {
        var vars = RedirectContainer.extractVariables("https://foo.atlassian.net{/browse/\$1}")
        assertEquals(1, vars.size)
        assertEquals("1", vars.first())

        vars = RedirectContainer.extractVariables("http://bar{/\$1/foo{/\$hi}}")
        assertEquals(2, vars.size)
        assertEquals("1", vars[0])
        assertEquals("hi", vars[1])
    }

    @Test
    fun `will ignore double $$`() {
        val vars = RedirectContainer.extractVariables("https://foo.atlassian.net{/browse/\$\$1}")
        assertEquals(0, vars.size)
    }
}