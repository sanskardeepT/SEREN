package com.seren.app.ui.tasks

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.seren.app.data.ConditionIds
import com.seren.app.ml.TfLiteManager
import com.seren.app.util.AudioFeatures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("MissingPermission")
@Composable
fun PhonologicalTaskScreen(
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val tfLiteManager = remember { TfLiteManager(context) }
    
    val startTime = remember { System.currentTimeMillis() }
    var isRecording by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val audioBuffer = remember { mutableListOf<Float>() }
    var recordingJob by remember { mutableStateOf<Job?>(null) }
    var activeRecord by remember { mutableStateOf<AudioRecord?>(null) }

    val colorsList = listOf(
        Color.Red to "Red",
        Color.Blue to "Blue",
        Color.Green to "Green",
        Color.Yellow to "Yellow",
        Color.Magenta to "Pink"
    )

    val startRecordingLogic = {
        synchronized(audioBuffer) { audioBuffer.clear() }
        isRecording = true
        recordingJob = coroutineScope.launch(Dispatchers.IO) {
            val sampleRate = 16000
            val channelConfig = AudioFormat.CHANNEL_IN_MONO
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            val bufferSize = if (minBufferSize > 0) minBufferSize else 2048
            
            val record = try {
                AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    bufferSize
                )
            } catch (e: SecurityException) {
                null
            }
            activeRecord = record
            
            if (record != null && record.state == AudioRecord.STATE_INITIALIZED) {
                try {
                    record.startRecording()
                    val tempBuffer = ShortArray(bufferSize)
                    while (isRecording) {
                        val read = record.read(tempBuffer, 0, tempBuffer.size)
                        if (read > 0) {
                            val floatList = ArrayList<Float>(read)
                            for (i in 0 until read) {
                                floatList.add(tempBuffer[i] / 32768.0f)
                            }
                            synchronized(audioBuffer) {
                                audioBuffer.addAll(floatList)
                            }
                        }
                    }
                    try { record.stop() } catch (e: Exception) {}
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    record.release()
                    activeRecord = null
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startRecordingLogic()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Task 4: Rapid Naming (RAN)",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Text(
                text = "Tap 'Start Recording' and name the colored blocks shown below from left to right as fast as you can.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Color card items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            colorsList.forEach { (color, name) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(color, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Recording CTA controls
        if (!isRecording) {
            Button(
                onClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                    if (hasPermission) {
                        startRecordingLogic()
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Mic")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Start Recording", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Button(
                onClick = {
                    isRecording = false
                    try {
                        activeRecord?.stop()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    recordingJob?.cancel()
                    
                    val duration = System.currentTimeMillis() - startTime
                    
                    val finalAudio = FloatArray(48000)
                    val rawList = synchronized(audioBuffer) { audioBuffer.toList() }
                    
                    if (rawList.isNotEmpty()) {
                        for (i in 0 until 48000) {
                            finalAudio[i] = if (i < rawList.size) rawList[i] else 0f
                        }
                    }
                    
                    val validAudioSize = minOf(48000, rawList.size)
                    val activeAudio = finalAudio.copyOfRange(0, validAudioSize)
                    
                    val silencePercent = AudioFeatures.calculateSilencePercentage(activeAudio)
                    val jitter = AudioFeatures.calculateAmplitudeJitter(activeAudio)
                    
                    val scores = tfLiteManager.runPhonNet(finalAudio)
                    val risk = 1f - scores[3]
                    
                    val rawJson = "{\"duration_ms\": $duration, \"silence_percentage\": $silencePercent, \"amplitude_jitter\": $jitter}"
                    onComplete(ConditionIds.DYSLEXIA_PHONOLOGICAL, risk, rawJson, duration)
                    onComplete(ConditionIds.DYSLEXIA_MIXED, risk, rawJson, duration)
                    onComplete(ConditionIds.EXPRESSIVE_LANGUAGE, risk, rawJson, duration)
                    onComplete(ConditionIds.RECEPTIVE_LANGUAGE, risk, rawJson, duration)
                    onComplete(ConditionIds.ANOMIA, risk, rawJson, duration)
                    onComplete(ConditionIds.PHONOLOGICAL_DISORDER, risk, rawJson, duration)
                    onComplete(ConditionIds.APD, risk, rawJson, duration)
                    
                    onNext()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(text = "Stop & Submit Speech", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}


