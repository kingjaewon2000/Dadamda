package com.example.apiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = [
    "com.example.core.domain.log",
    "com.example.apiserver"
])
@EnableJpaRepositories(basePackages = [
    "com.example.core.domain.log",
    "com.example.apiserver"
])
class ApiServerApplication

fun main(args: Array<String>) {
    runApplication<ApiServerApplication>(*args)
}
