package com.seren.app.ui.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seren.app.data.ConditionIds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateListOf

@Composable
fun PracticeScreen(
    onNavigateBack: () -> Unit,
    viewModel: PracticeViewModel = viewModel()
) {
    val scores by viewModel.latestScores.collectAsState()
    val streakCount by viewModel.streakCount.collectAsState()
    val isPracticeSaved by viewModel.isPracticeSaved.collectAsState()

    val scope = rememberCoroutineScope()
    var activeExerciseName by remember { mutableStateOf<String?>(null) }
    var exerciseTimerActive by remember { mutableStateOf(false) }
    var exerciseProgress by remember { mutableStateOf(0f) }
    
    // Trackers for completed exercises inside this session
    var completedExercises by remember { mutableStateOf(0) }
    val totalRequiredExercises = 2
    val completedTaskTitles = remember { mutableStateListOf<String>() }

    LaunchedEffect(isPracticeSaved) {
        if (isPracticeSaved) {
            onNavigateBack()
        }
    }

    // Determine targeted exercises based on scores
    val recommendations = remember(scores) {
        val list = mutableListOf<PracticeTaskItem>()
        
        val hasReadingRisk = scores.any { 
            (it.conditionId == ConditionIds.DYSLEXIA_PHONOLOGICAL || it.conditionId == ConditionIds.DYSLEXIA_SURFACE || it.conditionId == ConditionIds.DYSLEXIA_MIXED || it.conditionId == ConditionIds.DYSGRAPHIA_PHONOLOGICAL || it.conditionId == ConditionIds.DYSGRAPHIA_MOTOR || it.conditionId == ConditionIds.SLD_DYSLEXIA_DYSGRAPHIA || it.conditionId == ConditionIds.ADULT_DYSLEXIA) && it.riskScore > 40f 
        }
        val hasMathRisk = scores.any { 
            (it.conditionId == ConditionIds.DYSCALCULIA_CORE || it.conditionId == ConditionIds.DYSCALCULIA_ACCESS || it.conditionId == ConditionIds.SLD_DYSLEXIA_DYSCALCULIA || it.conditionId == ConditionIds.ADULT_DYSCALCULIA) && it.riskScore > 40f 
        }
        val hasAttentionRisk = scores.any { 
            (it.conditionId == ConditionIds.ADHD_INATTENTIVE || it.conditionId == ConditionIds.ADHD_HYPERACTIVE || it.conditionId == ConditionIds.ADHD_COMBINED || it.conditionId == ConditionIds.ADHD_ADULT || it.conditionId == ConditionIds.ADHD_DYSLEXIA_COMORBID || it.conditionId == ConditionIds.ADULT_ADHD) && it.riskScore > 40f 
        }
        val hasSpeechRisk = scores.any { 
            (it.conditionId == ConditionIds.STUTTERING_REPETITIONS || it.conditionId == ConditionIds.STUTTERING_PROLONGATIONS || it.conditionId == ConditionIds.STUTTERING_INTERJECTIONS || it.conditionId == ConditionIds.CLUTTERING || it.conditionId == ConditionIds.ANOMIA || it.conditionId == ConditionIds.PHONOLOGICAL_DISORDER || it.conditionId == ConditionIds.APD || it.conditionId == ConditionIds.APRAXIA_OF_SPEECH || it.conditionId == ConditionIds.EXPRESSIVE_LANGUAGE || it.conditionId == ConditionIds.RECEPTIVE_LANGUAGE || it.conditionId == ConditionIds.VOICE_DISORDER || it.conditionId == ConditionIds.DYSARTHRIA || it.conditionId == ConditionIds.ADULT_ANOMIA) && it.riskScore > 40f 
        }
        val hasAnxietyRisk = scores.any {
            (it.conditionId == ConditionIds.SOCIAL_ANXIETY || it.conditionId == ConditionIds.GAD || it.conditionId == ConditionIds.TEST_ANXIETY || it.conditionId == ConditionIds.SEPARATION_ANXIETY || it.conditionId == ConditionIds.SCHOOL_PHOBIA || it.conditionId == ConditionIds.MATHEMATICAL_ANXIETY) && it.riskScore > 40f
        }
        val hasDepressionRisk = scores.any {
            it.conditionId == ConditionIds.DEPRESSION && it.riskScore > 40f
        }
        val hasDysregulationRisk = scores.any {
            it.conditionId == ConditionIds.EMOTIONAL_DYSREGULATION && it.riskScore > 40f
        }
        val hasExecutiveMemoryRisk = scores.any {
            (it.conditionId == ConditionIds.EXECUTIVE_PLANNING || it.conditionId == ConditionIds.WORKING_MEMORY || it.conditionId == ConditionIds.VERBAL_WORKING_MEMORY || it.conditionId == ConditionIds.VISUAL_SPATIAL_MEM) && it.riskScore > 40f
        }
        val hasMutismRisk = scores.any {
            it.conditionId == ConditionIds.SELECTIVE_MUTISM && it.riskScore > 40f
        }

        if (hasReadingRisk) {
            list.add(PracticeTaskItem("Phoneme Segmentation", "Drag & connect matching phonemes to read sentences.", "Dyslexia Indicator Module"))
            list.add(PracticeTaskItem("Letter-Formation Canvas", "Copy Hindi characters highlighting kinematics alignment.", "Dysgraphia Indicator Module"))
        }
        if (hasMathRisk) {
            list.add(PracticeTaskItem("Subitizing Count", "Identify dot groups rapidly to improve number sense.", "Dyscalculia Indicator Module"))
        }
        if (hasAttentionRisk) {
            list.add(PracticeTaskItem("Breathing Focus Space", "Follow the expanding circle triggers to train focus.", "ADHD Attention Module"))
        }
        if (hasSpeechRisk) {
            list.add(PracticeTaskItem("Syllable Pacing", "Read the paragraph aligning with rhythmic sound ticks.", "Speech Indicator Module"))
        }
        if (hasAnxietyRisk) {
            list.add(PracticeTaskItem("Brave Explorer", "Practice micro-courage scenarios step-by-step to build confidence.", "Anxiety Coping Module"))
            list.add(PracticeTaskItem("Worry Externalisation (CBT)", "Externalise worries into separate boxes to restructure thoughts.", "GAD Worry Restructuring"))
        }
        if (hasMutismRisk) {
            list.add(PracticeTaskItem("Whisper-to-Voice Fading", "Practice speaking gentle syllables starting from a whisper to full voice.", "Selective Mutism Speech fading"))
        }
        if (hasDepressionRisk) {
            list.add(PracticeTaskItem("Positive Moments Log", "Recall and record positive events to build behavioral activation.", "Masked Depression Activation"))
        }
        if (hasDysregulationRisk) {
            list.add(PracticeTaskItem("DBT Calming Routines", "Trace calming visual trigger paths and regulate breathing.", "Emotional Calm Module"))
        }
        if (hasExecutiveMemoryRisk) {
            list.add(PracticeTaskItem("Sort the Stars (Switch)", "Practice rapid task-switching speed and categorization.", "Executive Control Training"))
            list.add(PracticeTaskItem("Memory Castle (Corsi)", "Recall spatial block patterns sequences to train memory spans.", "Working Memory Training"))
        }

        // Fill up to default if list size is small
        if (list.size < 2) {
            if (list.none { it.title == "Phoneme Segmentation" }) {
                list.add(PracticeTaskItem("Phoneme Segmentation", "Empower decoding loops with phonological games.", "Foundational Phonics"))
            }
            if (list.none { it.title == "Breathing Focus Space" }) {
                list.add(PracticeTaskItem("Breathing Focus Space", "Train sustained attention spans.", "Focus Training"))
            }
        }
        list
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Daily Practice Hub",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Streak Header Card with Star Motif
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily Practice Streak",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$streakCount Days Active",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Streak Star",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            // Daily progress bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Today's Task Completion",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$completedExercises / $totalRequiredExercises",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    val progress = completedExercises.toFloat() / totalRequiredExercises.toFloat()
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.tertiary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            Text(
                text = "Targeted Exercises for You",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            // Lists targeted exercises
            recommendations.forEach { task ->
                val taskDone = task.title in completedTaskTitles

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = task.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Category: ${task.reason}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        if (taskDone) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Finished",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(36.dp)
                            )
                        } else {
                            Button(
                                onClick = {
                                    activeExerciseName = task.title
                                    exerciseTimerActive = true
                                    exerciseProgress = 0f
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Start", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Completion submit trigger CTA
            AnimatedVisibility(visible = completedExercises >= totalRequiredExercises) {
                Button(
                    onClick = { viewModel.savePracticeSession() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        text = "Complete Today's Loop",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

    // Practice Simulation progress dialog modal
    if (exerciseTimerActive && activeExerciseName != null) {
        Dialog(onDismissRequest = { exerciseTimerActive = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = activeExerciseName!!,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    when (activeExerciseName) {
                        "Breathing Focus Space" -> {
                            var breathCycle by remember { mutableStateOf(1) }
                            var isHolding by remember { mutableStateOf(false) }

                            Text(
                                text = if (isHolding) "Hold and breathe slowly..." else "Press and Hold to Inhale",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )

                            Box(
                                modifier = Modifier
                                    .size(if (isHolding) 140.dp else 90.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                                    .clickable {
                                        isHolding = !isHolding
                                        if (!isHolding) {
                                            breathCycle++
                                            if (breathCycle > 3) {
                                                completedTaskTitles.add(activeExerciseName!!)
                                                completedExercises = minOf(completedExercises + 1, totalRequiredExercises)
                                                exerciseTimerActive = false
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isHolding) "Release" else "Hold",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "Breaths Complete: ${breathCycle - 1} / 3",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        "Subitizing Count" -> {
                            var dotCount by remember { mutableStateOf((2..6).random()) }
                            var correctAnswers by remember { mutableStateOf(0) }

                            Text(
                                text = "Pop/Tap the correct count below:",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(dotCount) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                listOf(2, 3, 4, 5, 6).forEach { num ->
                                    Button(
                                        onClick = {
                                            if (num == dotCount) {
                                                correctAnswers++
                                                if (correctAnswers >= 3) {
                                                    completedTaskTitles.add(activeExerciseName!!)
                                                    completedExercises = minOf(completedExercises + 1, totalRequiredExercises)
                                                    exerciseTimerActive = false
                                                } else {
                                                    dotCount = (2..6).random()
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text(num.toString(), fontSize = 12.sp)
                                    }
                                }
                            }

                            Text(
                                text = "Correct: $correctAnswers / 3",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        "Sort the Stars (Switch)" -> {
                            var shape by remember { mutableStateOf(listOf("Star", "Circle").random()) }
                            var color by remember { mutableStateOf(listOf("Red", "Blue").random()) }
                            var rule by remember { mutableStateOf(listOf("Color", "Shape").random()) }
                            var matchScore by remember { mutableStateOf(0) }

                            Text(
                                text = "Sorting Rule: Match by $rule",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )

                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$color $shape",
                                    color = if (color == "Red") Color.Red else Color.Cyan,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        val isCorrect = if (rule == "Color") color == "Red" else shape == "Star"
                                        if (isCorrect) {
                                            matchScore++
                                            if (matchScore >= 3) {
                                                completedTaskTitles.add(activeExerciseName!!)
                                                completedExercises = minOf(completedExercises + 1, totalRequiredExercises)
                                                exerciseTimerActive = false
                                            } else {
                                                shape = listOf("Star", "Circle").random()
                                                color = listOf("Red", "Blue").random()
                                                rule = listOf("Color", "Shape").random()
                                            }
                                        }
                                    }
                                ) {
                                    Text("Red/Star", fontSize = 10.sp)
                                }

                                Button(
                                    onClick = {
                                        val isCorrect = if (rule == "Color") color == "Blue" else shape == "Circle"
                                        if (isCorrect) {
                                            matchScore++
                                            if (matchScore >= 3) {
                                                completedTaskTitles.add(activeExerciseName!!)
                                                completedExercises = minOf(completedExercises + 1, totalRequiredExercises)
                                                exerciseTimerActive = false
                                            } else {
                                                shape = listOf("Star", "Circle").random()
                                                color = listOf("Red", "Blue").random()
                                                rule = listOf("Color", "Shape").random()
                                            }
                                        }
                                    }
                                ) {
                                    Text("Blue/Circle", fontSize = 10.sp)
                                }
                            }

                            Text(
                                text = "Score: $matchScore / 3",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        "Worry Externalisation (CBT)" -> {
                            var worryText by remember { mutableStateOf("") }
                            var isReleasing by remember { mutableStateOf(false) }

                            if (!isReleasing) {
                                Text(
                                    text = "Write your worry below and tap release to throw it to the stars.",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                OutlinedTextField(
                                    value = worryText,
                                    onValueChange = { worryText = it },
                                    placeholder = { Text("Write your worry here...") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        isReleasing = true
                                        scope.launch {
                                            delay(1500)
                                            completedTaskTitles.add(activeExerciseName!!)
                                            completedExercises = minOf(completedExercises + 1, totalRequiredExercises)
                                            exerciseTimerActive = false
                                        }
                                    },
                                    enabled = worryText.isNotBlank()
                                ) {
                                    Text("Release to Universe 🚀")
                                }
                            } else {
                                Text(
                                    text = "Releasing worry into the cosmic sky...",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                                )
                            }
                        }

                        else -> {
                            // Default interactive task tracker
                            Text(
                                text = "Tap complete to log practice.",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Button(
                                onClick = {
                                    completedTaskTitles.add(activeExerciseName!!)
                                    completedExercises = minOf(completedExercises + 1, totalRequiredExercises)
                                    exerciseTimerActive = false
                                }
                            ) {
                                Text("Complete Exercise")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class PracticeTaskItem(
    val title: String,
    val description: String,
    val reason: String
)
