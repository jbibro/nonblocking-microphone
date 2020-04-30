package com.bibro.microphone

import com.bibro.microphone.nonblocking.Recording
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.time.Duration
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem


class Signal internal constructor (
    private val wave: ByteArray,
    private val format: AudioFormat
) {

    fun duration() = wave.size / format.frameSize / format.frameRate

    suspend fun play() = coroutineScope {
        val line = AudioSystem.getSourceDataLine(format)
        line.run {
            open()
            start()
        }

        val total = wave.size
        var sent = 0

        while (sent < total) {
            val available = line.available()
            if (available > 0) {
                sent += line.write(
                    wave,
                    sent,
                    if (total - sent >= available) available else total - sent
                )
            }
            delay(100) // default buffer size is 0.5s, see AbstractDataLine
        }

        line.run {
            stop()
            close()
        }
    }

    companion object {
        fun capture(sampleRate: Float, sampleSize: Int, timeLimit: Duration) =
            Recording(
                sampleRate = sampleRate,
                sampleSize = sampleSize,
                duration = timeLimit
            )
    }
}