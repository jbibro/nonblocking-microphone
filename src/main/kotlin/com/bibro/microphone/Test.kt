package com.bibro.microphone

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.Executors

fun main() {
    val dispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    CoroutineScope(dispatcher).launch {
        Signal
            .capture(
                sampleRate = 44100f,
                sampleSize = 16,
                timeLimit = Duration.ofSeconds(5)
            )
            .start()
            .also {
                println("Captured ${it.duration()} seconds")
            }
            .play()
    }

    CoroutineScope(dispatcher).launch {
        delay(2000)
        println("I'm free while capturing microphone - ${Thread.currentThread().name}")
        delay(5000)
        println("I'm free while playing - ${Thread.currentThread().name}")
    }

    Thread.sleep(100000)
}