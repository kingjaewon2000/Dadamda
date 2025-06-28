package com.example.worker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = [
    "com.example.core.domain",
    "com.example.worker"
])
@EnableJpaRepositories(basePackages = [
    "com.example.core.domain",
    "com.example.worker"
])
@EnableElasticsearchRepositories(basePackages = [
    "com.example.core",
    "com.example.worker"
])
class WorkerApplication

fun main(args: Array<String>) {
    runApplication<WorkerApplication>(*args)
}
