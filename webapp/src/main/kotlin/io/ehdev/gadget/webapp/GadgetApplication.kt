package io.ehdev.gadget.webapp

import org.springframework.boot.SpringApplication

open class GadgetApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(MainConfiguration::class.java, *args)
        }
    }
}