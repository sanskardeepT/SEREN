package com.seren.app.ml

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HeuristicScorersTest {

    @Test
    fun testScoreDrawNet() {
        // Create 4D array [1][16][16][3] filled with black pixels (0f)
        val inputImage = Array(1) {
            Array(16) {
                Array(16) {
                    FloatArray(3) { 0f }
                }
            }
        }
        val result = HeuristicScorers.scoreDrawNet(inputImage)
        
        // Output size should be exactly 3
        assertEquals(3, result.size)
        // Intensity is 0, rawProb is 0, so output should be [1.0, 0.0, 0.0]
        assertEquals(1.0f, result[0], 0.001f)
        assertEquals(0.0f, result[1], 0.001f)
        assertEquals(0.0f, result[2], 0.001f)
    }

    @Test
    fun testScoreGazeNet_NoRegression() {
        // Create 3D array [1][5][6] representing forward eye movements
        val gazeSequence = Array(1) {
            arrayOf(
                floatArrayOf(10f, 100f, 0f, 0f, 0f, 1f),
                floatArrayOf(20f, 100f, 0f, 0f, 0f, 1f),
                floatArrayOf(30f, 100f, 0f, 0f, 0f, 1f),
                floatArrayOf(40f, 100f, 0f, 0f, 0f, 1f),
                floatArrayOf(50f, 100f, 0f, 0f, 0f, 1f)
            )
        }
        val result = HeuristicScorers.scoreGazeNet(gazeSequence)
        // No horizontal regressions, so score should be 0f
        assertEquals(0f, result, 0.001f)
    }

    @Test
    fun testScoreGazeNet_WithRegression() {
        // Horizontal regression at step 3 (current X < prev X and Y stable)
        val gazeSequence = Array(1) {
            arrayOf(
                floatArrayOf(10f, 100f, 0f, 0f, 0f, 1f),
                floatArrayOf(20f, 100f, 0f, 0f, 0f, 1f),
                floatArrayOf(15f, 100f, 0f, 0f, 0f, 1f), // regression
                floatArrayOf(30f, 100f, 0f, 0f, 0f, 1f),
                floatArrayOf(40f, 100f, 0f, 0f, 0f, 1f)
            )
        }
        val result = HeuristicScorers.scoreGazeNet(gazeSequence)
        // 1 regression over 5 frames = 0.2 ratio -> * 5 = 1f dyslexia risk
        assertEquals(1f, result, 0.001f)
    }

    @Test
    fun testScorePhonNet_Fluent() {
        // High energy signal (fully fluent)
        val audioData = FloatArray(48000) { 0.5f }
        val result = HeuristicScorers.scorePhonNet(audioData)
        
        // Output should be size 4
        assertEquals(4, result.size)
        // Highly fluent, block probability should be 0f
        assertEquals(0f, result[0], 0.001f)
        assertEquals(1f, result[3], 0.001f)
    }

    @Test
    fun testScorePhonNet_Disfluent() {
        // Silent signal (block risk)
        val audioData = FloatArray(48000) { 0f }
        val result = HeuristicScorers.scorePhonNet(audioData)
        
        // High block probabilities
        assertTrue(result[3] < 0.5f)
        assertTrue(result[2] > 0f)
    }

    @Test
    fun testScoreAttentNet_Typical() {
        // Low miss, commission, and gaze off stats
        val behaviorStats = floatArrayOf(0.02f, 0.01f, 0.05f, 0.03f)
        val result = HeuristicScorers.scoreAttentNet(behaviorStats)
        
        // Control probability should be highest
        assertEquals(4, result.size)
        assertTrue(result[0] > 0.8f)
        assertTrue(result[3] < 0.1f)
    }

    @Test
    fun testScoreAttentNet_ADHD() {
        // High miss rates and reaction-time variability
        val behaviorStats = floatArrayOf(0.85f, 0.85f, 0.85f, 0.85f)
        val result = HeuristicScorers.scoreAttentNet(behaviorStats)
        
        // Control probability should be low, combined/inattentive high
        assertTrue(result[0] < 0.2f)
        assertTrue(result[3] > 0.1f)
    }
}
