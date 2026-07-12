package com.seren.app.ui.practice

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool

object PracticeAudioAssetManager {
    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private val soundMap = HashMap<String, Int>()

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attrs)
            .build()
    }

    // Load sound assets by name dynamically to prevent compile errors if files are absent
    fun initializeSounds(context: Context) {
        val keys = listOf("voice_inhale", "voice_exhale", "voice_success", "voice_error", "voice_unblocked")
        keys.forEach { key ->
            val resId = context.resources.getIdentifier(key, "raw", context.packageName)
            if (resId != 0) {
                try {
                    val soundId = soundPool?.load(context, resId, 1) ?: 0
                    if (soundId != 0) {
                        soundMap[key] = soundId
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Play human voice prompt with fallback
    fun playVoicePrompt(context: Context, key: String) {
        val soundId = soundMap[key]
        if (soundId != null && soundId != 0) {
            soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
            when (key) {
                "voice_success" -> PracticeAudioHapticHelper.triggerHapticTick(context)
                "voice_error" -> PracticeAudioHapticHelper.triggerHapticError(context)
                else -> PracticeAudioHapticHelper.triggerHapticTick(context)
            }
        } else {
            // Fallback to tone generator
            when (key) {
                "voice_success" -> {
                    PracticeAudioHapticHelper.playSuccessChimeOnly()
                    PracticeAudioHapticHelper.triggerHapticTick(context)
                }
                "voice_error" -> {
                    PracticeAudioHapticHelper.playErrorBuzzOnly()
                    PracticeAudioHapticHelper.triggerHapticError(context)
                }
                else -> {
                    PracticeAudioHapticHelper.playTickChimeOnly()
                    PracticeAudioHapticHelper.triggerHapticTick(context)
                }
            }
        }
    }

    // Loop background game music dynamically by resource name
    fun playBackgroundMusic(context: Context, musicName: String) {
        stopBackgroundMusic()
        val resId = context.resources.getIdentifier(musicName, "raw", context.packageName)
        if (resId != 0) {
            try {
                mediaPlayer = MediaPlayer.create(context, resId).apply {
                    isLooping = true
                    setVolume(0.35f, 0.35f) // Calmed background volume
                    start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stopBackgroundMusic() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
