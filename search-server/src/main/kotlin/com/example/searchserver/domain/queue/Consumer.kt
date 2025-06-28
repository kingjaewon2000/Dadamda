package com.example.searchserver.domain.queue

interface Consumer<T> {

    fun consume(): List<T>

}