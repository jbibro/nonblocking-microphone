package com.bibro.microphone

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration

fun main() = runBlocking<Unit> {
    launch {
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

    launch {
        delay(2000)
        println("I'm free while capturing microphone - ${Thread.currentThread().name}")
        delay(5000)
        println("I'm free while playing - ${Thread.currentThread().name}")
    }
}