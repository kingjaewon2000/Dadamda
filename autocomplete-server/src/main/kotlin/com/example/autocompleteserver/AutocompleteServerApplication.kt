package com.example.autocompleteserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@SpringBootApplication
@EntityScan(basePackages = [
	"com.example.core.domain.autocomplete",
	"com.example.autocompleteserver",
])
@EnableElasticsearchRepositories(basePackages = [
	"com.example.core.domain.autocomplete",
	"com.example.autocompleteserver"
])
class AutocompleteServerApplication

fun main(args: Array<String>) {
	runApplication<AutocompleteServerApplication>(*args)
}
