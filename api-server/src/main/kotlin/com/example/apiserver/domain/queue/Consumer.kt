package com.example.apiserver.domain.queue

interface Consumer<T> {

    fun consume(): List<T>

}