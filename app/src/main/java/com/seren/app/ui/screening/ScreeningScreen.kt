package com.seren.app.ui.screening

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seren.app.data.ConditionIds
import com.seren.app.data.SerenDatabase
import com.seren.app.data.model.ConfidenceLevel
import com.seren.app.data.model.ConditionScore
import com.seren.app.data.model.ScreeningSession
import com.seren.app.data.model.SessionStatus
import com.seren.app.data.model.SessionType
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun ScreeningScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { SerenDatabase.getDatabase(context) }
    
    var currentSessionId by remember { mutableStateOf<Long?>(null) }
    var isSimulating by remember { mutableStateOf(false) }

    // Create session on launch
    LaunchedEffect(key1 = true) {
        scope.launch {
            val profile = database.userDao().getUserProfile()
            if (profile != null) {
                val newSession = ScreeningSession(
                    userId = profile.userId,
                    sessionType = SessionType.SCREENING,
                    status = SessionStatus.IN_PROGRESS
                )
                val id = database.screeningDao().insertSession(newSession)
                currentSessionId = id
            }
        }
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
                    text = "Screening Battery (Batch 1)",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Task status warning card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Task Engines Pending (Mission 3)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "The on-device tasks (Handwriting/DrawNet, Reading/GazeNet, Speech/PhonNet, CPT/AttentNet) will be wired in Mission 3. For now, you can simulate a session completion below to populate the database and verify scoring streams.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Conditions under assessment
            Text(
                text = "Conditions Under Assessment",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            ConditionIds.BATCH_1.forEach { conditionId ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = ConditionIds.getDisplayName(conditionId),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = ConditionIds.getDescription(conditionId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action: Simulate and Complete
            Button(
                onClick = {
                    val sessionId = currentSessionId ?: return@Button
                    isSimulating = true
                    scope.launch {
                        // Generate dummy condition scores for validation purposes
                        val dummyScores = ConditionIds.BATCH_1.map { conditionId ->
                            val scoreValue = Random.nextInt(10, 85).toFloat()
                            val confidence = when {
                                scoreValue < 40f -> ConfidenceLevel.LOW
                                scoreValue < 70f -> ConfidenceLevel.MEDIUM
                                else -> ConfidenceLevel.HIGH
                            }
                            ConditionScore(
                                sessionId = sessionId,
                                conditionId = conditionId,
                                riskScore = scoreValue,
                                confidenceLevel = confidence,
                                modalitiesUsed = Random.nextInt(1, 4)
                            )
                        }
                        
                        database.screeningDao().insertConditionScores(dummyScores)
                        database.screeningDao().updateSessionStatus(
                            sessionId = sessionId,
                            completedAt = System.currentTimeMillis(),
                            status = SessionStatus.COMPLETED
                        )
                        
                        isSimulating = false
                        onNavigateBack()
                    }
                },
                enabled = currentSessionId != null && !isSimulating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Simulate")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSimulating) "Simulating..." else "Simulate Session Completion",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
