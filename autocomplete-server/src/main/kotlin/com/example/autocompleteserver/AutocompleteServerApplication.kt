package com.example.autocompleteserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AutocompleteServerApplication

fun main(args: Array<String>) {
	runApplication<AutocompleteServerApplication>(*args)
}
