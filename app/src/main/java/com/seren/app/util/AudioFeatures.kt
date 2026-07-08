package com.seren.app.util

import kotlin.math.abs

/**
 * Shared audio feature extraction utilities used by both
 * PhonologicalTaskScreen and SpeechFluencyTaskScreen.
 */
object AudioFeatures {

    /**
     * Calculates the percentage of silent samples in an audio buffer.
     * A sample is considered silent if its absolute value is below [threshold].
     */
    fun calculateSilencePercentage(audio: FloatArray, threshold: Float = 0.01f): Float {
        if (audio.isEmpty()) return 0f
        val silentSamples = audio.count { abs(it) < threshold }
        return (silentSamples.toFloat() / audio.size) * 100f
    }

    /**
     * Calculates the average absolute amplitude difference between consecutive samples.
     * Higher jitter indicates more irregular vocal output.
     */
    fun calculateAmplitudeJitter(audio: FloatArray): Float {
        if (audio.size < 2) return 0f
        var diffSum = 0f
        var count = 0
        for (i in 1 until audio.size) {
            diffSum += abs(abs(audio[i]) - abs(audio[i - 1]))
            count++
        }
        return diffSum / count
    }
}
