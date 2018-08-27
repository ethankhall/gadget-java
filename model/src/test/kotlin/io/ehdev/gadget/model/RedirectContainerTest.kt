package io.ehdev.gadget.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RedirectContainerTest {

    @Test
    fun `without variable`() {
        val redirect = RedirectContainer("/foo", emptyList(), "http://bar")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar flig", redirect.buildRedirect("/foo%20flig"))
    }

    @Test
    fun `with variable`() {
        val redirect = RedirectContainer("/foo", listOf("1"), "http://bar{/\$1/foo}")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar/flig/foo", redirect.buildRedirect("/foo%20flig"))
    }

    @Test
    fun `with variables`() {
        val redirect = RedirectContainer("/foo", listOf("1", "hi"), "http://bar{/\$1/foo{/\$hi}}")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar/flig/foo", redirect.buildRedirect("/foo%20flig"))
        assertEquals("http://bar/flig/foo/blarg", redirect.buildRedirect("/foo%20flig%20blarg"))
    }

    @Test
    fun `with extra variables`() {
        val redirect = RedirectContainer("/foo", listOf("1"), "http://bar{/\$1/foo}")

        assertEquals("http://bar", redirect.buildRedirect("/foo"))
        assertEquals("http://bar/flig/foo", redirect.buildRedirect("/foo%20flig"))
        assertEquals("http://bar/flig/foo blarg", redirect.buildRedirect("/foo%20flig%20blarg"))
    }
}