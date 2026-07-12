package com.seren.app.ui.practice

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object PracticeAudioHapticHelper {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85) // 85% volume
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playSuccessFeedback(context: Context) {
        PracticeAudioAssetManager.playVoicePrompt(context, "voice_success")
    }

    fun playErrorFeedback(context: Context) {
        PracticeAudioAssetManager.playVoicePrompt(context, "voice_error")
    }

    fun playTickFeedback(context: Context) {
        PracticeAudioAssetManager.playVoicePrompt(context, "voice_unblocked")
    }

    // Sound-only synthesis methods for fallback
    fun playSuccessChimeOnly() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 150)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playErrorBuzzOnly() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 250)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playTickChimeOnly() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 50)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun triggerHapticTick(context: Context) {
        vibrate(context, 20)
    }

    fun triggerHapticError(context: Context) {
        vibratePattern(context, longArrayOf(0, 80, 40, 80))
    }

    private fun vibrate(context: Context, durationMs: Long) {
        try {
            val vibrator = getVibrator(context) ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun vibratePattern(context: Context, pattern: LongArray) {
        try {
            val vibrator = getVibrator(context) ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getVibrator(context: Context): Vibrator? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            null
        }
    }
}
