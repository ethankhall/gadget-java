package io.ehdev.gadget.webapp.api.model

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

internal class RedirectDefinitionTest {

    @Test
    fun `alias doesn't have gadget`() {
        control {
            doValidation("aaaa", emptyList(), "http://localhost")
        }

        validate {
            val validation = doValidation("gadget")
            assertThat(validation, hasSize(1))
            assertThat(validation.first(), `is`("alias cannot start with 'gadget'"))
        }

        validate {
            val validation = doValidation("gadget/foo")
            assertThat(validation, hasSize(1))
            assertThat(validation.first(), `is`("alias cannot start with 'gadget'"))
        }
    }

    @Test
    fun `alias size between 2 and 128`() {
        control {
            doValidation("aaa")
        }

        validate {
            val validation = doValidation("a")
            assertThat(validation, hasSize(1))
            assertThat(validation.first(), `is`("alias must be between 2 and 128 char's long"))
        }

        validate {
            val validation = doValidation("a".repeat(200))
            assertThat(validation, hasSize(1))
            assertThat(validation.first(), `is`("alias must be between 2 and 128 char's long"))
        }
    }

    @Test
    fun `destination size between 2 and 4096`() {
        control {
            doValidation(destination = "http://example.com")
        }

        validate {
            val validation = doValidation(destination = "a.to")
            assertThat(validation, hasSize(1))
            assertThat(validation.first(), `is`("destination must be between 5 and 4096 char's long"))
        }

        validate {
            val validation = doValidation(destination = "a".repeat(5000) + ".com")
            assertThat(validation, hasSize(1))
            assertThat(validation.first(), `is`("destination must be between 5 and 4096 char's long"))
        }
    }

    private fun control(body: () -> List<String>) {
        val validation = body.invoke()
        assertThat(validation, hasSize(0))
    }

    private fun validate(body: () -> Unit) = body.invoke()

    private fun doValidation(alias: String = "hello-world",
                             variables: List<String> = emptyList(),
                             destination: String = "http://localhost"): List<String> {
        val results = RedirectDefinition(alias, variables, destination).validateModel().validationError
        if (results.isNotEmpty()) {
            println("<=================================>")
            results.forEach { println("\t - $it") }
        }
        return results
    }
}

