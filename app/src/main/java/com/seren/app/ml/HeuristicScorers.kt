package com.seren.app.ml

import kotlin.math.absoluteValue

/**
 * Pure heuristic scoring functions extracted from TfLiteManager fallback paths.
 * These contain the actual domain logic currently used for screening indicators.
 * They are standalone pure functions: no Android Context, no Interpreter, no side effects.
 *
 * When trained TFLite models are available, these serve as fallbacks only.
 */
object HeuristicScorers {

    /**
     * DrawNet heuristic: estimates letter reversal risk from pixel intensity.
     * Input: 4D image array [1][H][W][3], samples every 8th pixel in red channel.
     * Output: FloatArray(3) = [Normal, Reversal, Corrected] probabilities.
     */
    fun scoreDrawNet(inputImage: Array<Array<Array<FloatArray>>>): FloatArray {
        var sum = 0f
        var count = 0
        for (i in inputImage[0].indices step 8) {
            for (j in inputImage[0][i].indices step 8) {
                sum += inputImage[0][i][j][0] // Red channel
                count++
            }
        }
        val intensity = if (count > 0) sum / count else 0f

        // Simulating some pseudo-randomness based on pixel density
        val rawProb = (intensity * 10f).absoluteValue % 1f
        return floatArrayOf(1f - rawProb, rawProb * 0.7f, rawProb * 0.3f)
    }

    /**
     * GazeNet heuristic: counts backward eye-movement regressions in a gaze sequence.
     * Input: 3D array [1][timesteps][6] where features are [x, y, dx, dy, speed, is_fixation].
     * Output: Float 0.0-1.0 representing dyslexia risk (higher regressions = higher risk).
     */
    fun scoreGazeNet(gazeSequence: Array<Array<FloatArray>>): Float {
        var regressionCount = 0
        for (i in 1 until gazeSequence[0].size) {
            val prevX = gazeSequence[0][i - 1][0]
            val currX = gazeSequence[0][i][0]
            val prevY = gazeSequence[0][i - 1][1]
            val currY = gazeSequence[0][i][1]

            // Backward horizontal movement while Y is roughly stable = regression
            if (currX < prevX && (currY - prevY).absoluteValue < 20f) {
                regressionCount++
            }
        }

        val ratio = regressionCount.toFloat() / gazeSequence[0].size.toFloat()
        return (ratio * 5f).coerceIn(0f, 1f)
    }

    /**
     * PhonNet heuristic: measures silence-to-speech ratio in raw audio samples.
     * Input: FloatArray of 48000 samples (3 seconds @ 16kHz).
     * Output: FloatArray(4) = [Repetition, Prolongation, Block, Fluent] probabilities.
     */
    fun scorePhonNet(audioData: FloatArray): FloatArray {
        var silenceFrames = 0
        val frameSize = 256
        for (i in audioData.indices step frameSize) {
            var energy = 0f
            val endIdx = minOf(i + frameSize, audioData.size)
            for (j in i until endIdx) {
                energy += audioData[j] * audioData[j]
            }
            if (energy < 0.005f) {
                silenceFrames++
            }
        }

        val totalFrames = audioData.size / frameSize
        val silenceRatio = silenceFrames.toFloat() / totalFrames.toFloat()

        // High silence maps to speech block/stutter risk
        val blockProb = (silenceRatio * 2f).coerceIn(0f, 0.8f)
        val fluentProb = 1f - blockProb
        return floatArrayOf(blockProb * 0.4f, blockProb * 0.4f, blockProb * 0.2f, fluentProb)
    }

    /**
     * AttentNet heuristic: maps CPT behavioral stats to ADHD subtypes.
     * Input: FloatArray(4) = [miss_rate, commission_rate, rt_variability, gaze_off_task_pct].
     * Output: FloatArray(4) = [Control, Inattentive, Hyperactive, Combined] probabilities.
     */
    fun scoreAttentNet(behaviorStats: FloatArray): FloatArray {
        val missRate = behaviorStats[0]
        val commRate = behaviorStats[1]
        val gazeOff = behaviorStats[3]

        val inattentiveScore = ((missRate + gazeOff) / 2f).coerceIn(0f, 1f)
        val hyperactiveScore = commRate.coerceIn(0f, 1f)
        val combinedScore = (inattentiveScore * hyperactiveScore).coerceIn(0f, 1f)
        val typicalScore = (1f - maxOf(inattentiveScore, hyperactiveScore, combinedScore)).coerceIn(0f, 1f)

        val sum = typicalScore + inattentiveScore + hyperactiveScore + combinedScore
        return floatArrayOf(
            typicalScore / sum,
            inattentiveScore / sum,
            hyperactiveScore / sum,
            combinedScore / sum
        )
    }

    /**
     * EmotNet heuristic: keyword-based sentiment classification from transcript text.
     * Input: String transcript.
     * Output: FloatArray(4) = [Control, Worry, Perfectionism, Sadness] probabilities.
     */
    fun scoreEmotNet(textInput: String): FloatArray {
        val lowerText = textInput.lowercase()
        val worryCount = listOf("worry", "fail", "afraid", "scared", "test", "exam", "stomach").count { lowerText.contains(it) }
        val perfectionismCount = listOf("perfect", "mistake", "erase", "redo", "correct").count { lowerText.contains(it) }
        val sadnessCount = listOf("tired", "alone", "sad", "unhappy", "cry", "give up").count { lowerText.contains(it) }

        val worryProb = (worryCount * 0.35f).coerceIn(0f, 0.9f)
        val perfectionismProb = (perfectionismCount * 0.35f).coerceIn(0f, 0.9f)
        val sadnessProb = (sadnessCount * 0.35f).coerceIn(0f, 0.9f)
        val controlProb = (1f - maxOf(worryProb, perfectionismProb, sadnessProb)).coerceIn(0.1f, 1f)

        val sum = controlProb + worryProb + perfectionismProb + sadnessProb
        return floatArrayOf(
            controlProb / sum,
            worryProb / sum,
            perfectionismProb / sum,
            sadnessProb / sum
        )
    }

    /**
     * SpatialNet heuristic: maps Corsi block-tapping stats to memory/executive deficit risk.
     * Input: FloatArray(4) = [corsi_span, planning_time_ms, error_count, sequence_length].
     * Output: FloatArray(4) = [Control, MemoryDeficit, ExecutiveDeficit, Combined] probabilities.
     */
    fun scoreSpatialNet(spatialStats: FloatArray): FloatArray {
        val span = spatialStats[0]
        val planningTime = spatialStats[1]
        val errors = spatialStats[2]
        val targetLen = spatialStats[3]

        val spanLoss = (targetLen - span).coerceAtLeast(0f)

        val memoryScore = (spanLoss * 0.3f + errors * 0.15f).coerceIn(0f, 1f)
        val executiveScore = (planningTime / 5000f).coerceIn(0f, 1f)
        val combinedScore = (memoryScore * executiveScore).coerceIn(0f, 1f)
        val typicalScore = (1f - maxOf(memoryScore, executiveScore, combinedScore)).coerceIn(0f, 1f)

        val sum = typicalScore + memoryScore + executiveScore + combinedScore
        return floatArrayOf(
            typicalScore / sum,
            memoryScore / sum,
            executiveScore / sum,
            combinedScore / sum
        )
    }
}
