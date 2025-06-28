package com.example.searchserver.domain.queue

interface Producer<T> {

    fun produce(data: T)

}