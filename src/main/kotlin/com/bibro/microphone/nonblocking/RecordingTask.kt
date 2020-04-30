package com.bibro.microphone.nonblocking

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import javax.sound.sampled.TargetDataLine

internal class RecordingTask(
    private val line: TargetDataLine,
    private val maxBytes: Int
) {
    suspend fun record(): ByteArray = coroutineScope {
        line.run {
            open()
            start()
        }
        val wave = ByteArrayOutputStream()
        val buffer = ByteArray(line.bufferSize)

        while (wave.size() < maxBytes) {
            val available = line.available()
            if (available > 0) {
                line.read(buffer, 0, available)
                wave.write(buffer, 0, available)
            }
            delay(100) // default buffer size is 0.5s, see AbstractDataLine
        }
        line.run {
            stop()
            close()
        }

        wave.toByteArray()
    }
}