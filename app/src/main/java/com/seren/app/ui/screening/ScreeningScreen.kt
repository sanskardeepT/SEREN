package com.seren.app.ui.screening

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seren.app.ui.tasks.AttentionTaskScreen
import com.seren.app.ui.tasks.HandwritingTaskScreen
import com.seren.app.ui.tasks.NumberTaskScreen
import com.seren.app.ui.tasks.PhonologicalTaskScreen
import com.seren.app.ui.tasks.ReadingGazeTaskScreen
import com.seren.app.ui.tasks.SpeechFluencyTaskScreen

@Composable
fun ScreeningScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReport: (sessionId: Long) -> Unit,
    viewModel: ScreeningViewModel = viewModel()
) {
    val currentTaskIndex by viewModel.currentTaskIndex.collectAsState()
    val isSessionComplete by viewModel.isSessionComplete.collectAsState()
    val sessionId by viewModel.sessionId.collectAsState()

    // Start session when entered
    LaunchedEffect(key1 = true) {
        viewModel.startSession()
    }

    // Return home once session wraps up successfully
    LaunchedEffect(isSessionComplete) {
        if (isSessionComplete && sessionId != null) {
            onNavigateToReport(sessionId!!)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (currentTaskIndex) {
            0 -> HandwritingTaskScreen(
                onComplete = { conditionId, score, rawJson, duration ->
                    viewModel.submitTaskResult(conditionId, "drawnet_writing", rawJson, score, duration)
                },
                onNext = { viewModel.nextTask() }
            )
            1 -> NumberTaskScreen(
                onComplete = { conditionId, score, rawJson, duration ->
                    viewModel.submitTaskResult(conditionId, "numnet_counting", rawJson, score, duration)
                },
                onNext = { viewModel.nextTask() }
            )
            2 -> ReadingGazeTaskScreen(
                onComplete = { conditionId, score, rawJson, duration ->
                    viewModel.submitTaskResult(conditionId, "gazenet_gaze", rawJson, score, duration)
                },
                onNext = { viewModel.nextTask() }
            )
            3 -> PhonologicalTaskScreen(
                onComplete = { conditionId, score, rawJson, duration ->
                    viewModel.submitTaskResult(conditionId, "phonnet_ran", rawJson, score, duration)
                },
                onNext = { viewModel.nextTask() }
            )
            4 -> AttentionTaskScreen(
                onComplete = { conditionId, score, rawJson, duration ->
                    viewModel.submitTaskResult(conditionId, "attentnet_cpt", rawJson, score, duration)
                },
                onNext = { viewModel.nextTask() }
            )
            5 -> SpeechFluencyTaskScreen(
                onComplete = { conditionId, score, rawJson, duration ->
                    viewModel.submitTaskResult(conditionId, "phonnet_speech", rawJson, score, duration)
                },
                onNext = { viewModel.nextTask() }
            )
        }
    }
}
