package com.seren.app.ui.screening

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seren.app.ui.tasks.AttentionTaskScreen
import com.seren.app.ui.tasks.HandwritingTaskScreen
import com.seren.app.ui.tasks.NumberTaskScreen
import com.seren.app.ui.tasks.PhonologicalTaskScreen
import com.seren.app.ui.tasks.ReadingGazeTaskScreen
import com.seren.app.ui.tasks.SpeechFluencyTaskScreen
import com.seren.app.ui.tasks.QuestionnaireTaskScreen

@Composable
fun ScreeningScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReport: (sessionId: Long) -> Unit,
    viewModel: ScreeningViewModel = viewModel()
) {
    val currentTaskIndex by viewModel.currentTaskIndex.collectAsState()
    val isSessionComplete by viewModel.isSessionComplete.collectAsState()
    val sessionId by viewModel.sessionId.collectAsState()

    var isIntroActive by remember { mutableStateOf(true) }

    // Start session when entered
    LaunchedEffect(key1 = true) {
        viewModel.startSession()
    }

    // Reset intro status when task index changes
    LaunchedEffect(currentTaskIndex) {
        isIntroActive = true
    }

    LaunchedEffect(isSessionComplete) {
        val currentSessionId = sessionId
        if (isSessionComplete && currentSessionId != null) {
            onNavigateToReport(currentSessionId)
        }
    }

    val taskTitle = when (currentTaskIndex) {
        0 -> "Handwriting & Drawing"
        1 -> "Number Operations"
        2 -> "Reading Gaze"
        3 -> "Rapid Naming (RAN)"
        4 -> "Attention & Focus"
        5 -> "Speech & Fluency"
        else -> "Self-Report Questionnaire"
    }

    val taskInstruction = when (currentTaskIndex) {
        0 -> "We'll look at your handwriting speed and shapes to evaluate letter patterns. Draw the letter requested inside the box!"
        1 -> "We'll count dots and match numbers as quickly as possible. Keep your touch reactions steady!"
        2 -> "We'll look at how your eyes move to learn about your focus. Just read the text and keep your face visible!"
        3 -> "We'll trace voice naming rates. Name the colors from left to right as fast as you can!"
        4 -> "We'll test target inhibition control. Tap on letters when they appear, except if the letter is X!"
        5 -> "We'll record disfluency rates. Read the sentence aloud when recording starts!"
        else -> "Please answer the self-report questions to help us understand your emotional and sensory needs."
    }

    if (isIntroActive) {
        // --- SCREEN 9: TASK INTRO ---
        Scaffold(
            bottomBar = {
                Button(
                    onClick = { isIntroActive = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Start Task", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.background)
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Task Titles
                Text(
                    text = taskTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Task ${currentTaskIndex + 1} of 7",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Vector Eye Illustration Placeholder
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Circle Frame representing the eye with a floating star
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Iris representation
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.secondary, CircleShape)
                        )
                        // Floating star icon
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp).align(Alignment.TopEnd).padding(top = 4.dp, end = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Intros Card
                Text(
                    text = "Ready for a fun game?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = taskInstruction,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    } else {
        // --- SCREEN 10: ACTIVE SCREEN WRAPPER ---
        Scaffold(
            topBar = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        Text(
                            text = "Task ${currentTaskIndex + 1} of 7",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(48.dp)) // Maintain horizontal centering offsets
                    }
                    
                    // Progress Bar
                    val progress = (currentTaskIndex + 1).toFloat() / 7f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                            .height(8.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "You're doing great!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Screening in progress",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                    6 -> {
                        val userAgeGroup by viewModel.userAgeGroup.collectAsState()
                        val role = when {
                            userAgeGroup?.startsWith("child") == true -> "parent"
                            userAgeGroup?.startsWith("teen") == true -> "teen"
                            else -> "adult"
                        }
                        QuestionnaireTaskScreen(
                            userRole = role,
                            onComplete = { conditionId, score, rawJson, duration ->
                                viewModel.submitTaskResult(conditionId, "self_report_q", rawJson, score, duration)
                            },
                            onNext = { viewModel.nextTask() }
                        )
                    }
                }
            }
        }
    }
}
