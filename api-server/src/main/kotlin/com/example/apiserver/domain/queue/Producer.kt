package com.example.apiserver.domain.queue

interface Producer<T> {

    fun produce(data: T)

}