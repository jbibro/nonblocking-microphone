package com.bibro.microphone

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.time.Duration

private val logger = KotlinLogging.logger {}

fun main() = runBlocking {
    launch {
        Signal
                .capture(
                        sampleRate = 44100f,
                        sampleSize = 16,
                        timeLimit = Duration.ofSeconds(5)
                )
                .startAsync()
                .await()
                .play()
    }
    delay(1000)
    logger.debug { "I'm free" }
}