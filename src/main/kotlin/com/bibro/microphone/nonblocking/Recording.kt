package com.bibro.microphone.nonblocking

import com.bibro.microphone.Signal
import kotlinx.coroutines.coroutineScope
import java.time.Duration
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

class Recording(
    private val sampleRate: Float,
    private val sampleSize: Int,
    private val duration: Duration
) {
    suspend fun start() = coroutineScope {
        val format = AudioFormat(
            sampleRate,
            sampleSize,
            1,
            true,
            true
        )
        val line = AudioSystem.getTargetDataLine(format)
        val task = RecordingTask(
            line,
            duration.toBytes(format)
        )

        Signal(task.record(), format)
    }
}

fun Duration.toBytes(format: AudioFormat) =
    this.let { it.toSeconds() * format.frameRate * format.frameSize }.toInt()
