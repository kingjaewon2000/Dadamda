package com.example.searchserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = [
    "com.example.core.domain",
    "com.example.searchserver"
])
@EnableJpaRepositories(basePackages = [
    "com.example.core.domain",
    "com.example.searchserver"
])
@EnableElasticsearchRepositories(basePackages = [
    "com.example.core.domain",
])
class SearchServerApplication

fun main(args: Array<String>) {
    runApplication<SearchServerApplication>(*args)
}
