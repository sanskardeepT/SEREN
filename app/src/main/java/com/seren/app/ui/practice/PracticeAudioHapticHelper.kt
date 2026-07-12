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
        // Play clean success chime (Proprietary Acknowledgment Tone)
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 150)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        vibrate(context, 20) // Crisp tick
    }

    fun playErrorFeedback(context: Context) {
        // Play error buzz (CDMA negative acknowledgment tone)
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 250)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Double vibration buzz
        vibratePattern(context, longArrayOf(0, 80, 40, 80))
    }

    fun playTickFeedback(context: Context) {
        // Play short click/tick (CDMA pip tone)
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 50)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        vibrate(context, 10) // Micro-tick
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
