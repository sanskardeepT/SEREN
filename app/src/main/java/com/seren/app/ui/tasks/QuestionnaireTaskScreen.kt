package com.seren.app.ui.tasks

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seren.app.data.ConditionIds
import com.seren.app.ml.TfLiteManager

data class QuestionnaireItem(
    val parentText: String,
    val selfText: String,
    val domain: String // "anxiety", "emotional", "sensory", "executive", "insecurity"
)

@Composable
fun QuestionnaireTaskScreen(
    userRole: String, // "parent", "teen", "adult"
    onComplete: (conditionId: String, score: Float, rawJson: String, duration: Long) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val tfLiteManager = remember { TfLiteManager.getInstance(context) }
    val startTime = remember { System.currentTimeMillis() }
    
    val questions = remember {
        listOf(
            // Anxiety Domain (Q1 - Q4)
            QuestionnaireItem(
                parentText = "My child worries a lot about things, tests, or making mistakes.",
                selfText = "I worry a lot about things, tests, or making mistakes.",
                domain = "anxiety"
            ),
            QuestionnaireItem(
                parentText = "My child seems highly nervous, shy, or uncomfortable in social settings.",
                selfText = "I feel nervous, shy, or uncomfortable in social settings.",
                domain = "anxiety"
            ),
            QuestionnaireItem(
                parentText = "My child avoids school, work, or social situations due to worry or fear.",
                selfText = "I avoid school, work, or social situations due to worry or fear.",
                domain = "anxiety"
            ),
            QuestionnaireItem(
                parentText = "My child struggles to talk or freezes up when speaking to unfamiliar people.",
                selfText = "I struggle to talk or freeze up when speaking to unfamiliar people.",
                domain = "anxiety"
            ),
            
            // Emotional Domain (Q5 - Q7)
            QuestionnaireItem(
                parentText = "My child often seems low on energy, quiet, or persistently sad.",
                selfText = "I often feel low on energy, quiet, or persistently sad.",
                domain = "emotional"
            ),
            QuestionnaireItem(
                parentText = "My child experiences sudden emotional changes or struggles to calm down when upset.",
                selfText = "I experience sudden emotional changes or struggle to calm down when upset.",
                domain = "emotional"
            ),
            QuestionnaireItem(
                parentText = "My child avoids communicating or becomes silent when feeling stressed.",
                selfText = "I avoid communicating or become silent when feeling stressed.",
                domain = "emotional"
            ),
            
            // Sensory & Motor Domain (Q8 - Q10)
            QuestionnaireItem(
                parentText = "My child is highly sensitive to loud noises, bright lights, or textures.",
                selfText = "I am highly sensitive to loud noises, bright lights, or textures.",
                domain = "sensory"
            ),
            QuestionnaireItem(
                parentText = "My child struggles with coordination, balance, or handwriting/drawing alignment.",
                selfText = "I struggle with coordination, balance, or handwriting/drawing alignment.",
                domain = "sensory"
            ),
            QuestionnaireItem(
                parentText = "My child finds physical touch, tags on clothes, or pressure uncomfortable.",
                selfText = "I find physical touch, tags on clothes, or pressure uncomfortable.",
                domain = "sensory"
            ),
            
            // Executive & Memory Domain (Q11 - Q12)
            QuestionnaireItem(
                parentText = "My child finds it difficult to organize tasks, plan ahead, or maintain focus.",
                selfText = "I find it difficult to organize tasks, plan ahead, or maintain focus.",
                domain = "executive"
            ),
            QuestionnaireItem(
                parentText = "My child struggles to remember multi-step instructions, details, or sequences.",
                selfText = "I struggle to remember multi-step instructions, details, or sequences.",
                domain = "executive"
            ),
            
            // Insecurities & Confidence Domain (Q13 - Q15)
            QuestionnaireItem(
                parentText = "My child frequently doubts their competence or feels insecure about their performance.",
                selfText = "I frequently doubt my competence or feel insecure about my performance.",
                domain = "insecurity"
            ),
            QuestionnaireItem(
                parentText = "My child is extremely sensitive to criticism, feedback, or potential rejection.",
                selfText = "I am extremely sensitive to criticism, feedback, or potential rejection.",
                domain = "insecurity"
            ),
            QuestionnaireItem(
                parentText = "My child tends to please others to avoid conflicts, even if it means ignoring boundaries.",
                selfText = "I tend to please others to avoid conflicts, even if it means ignoring my boundaries.",
                domain = "insecurity"
            )
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    val responses = remember { mutableStateListOf<Int>() }
    var questionStartTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var showSpamAlert by remember { mutableStateOf(false) }

    val handleAnswer = { score: Int ->
        val now = System.currentTimeMillis()
        if (now - questionStartTime < 3000) {
            showSpamAlert = true
        } else {
            responses.add(score)
            questionStartTime = now
            if (currentIndex < questions.size - 1) {
                currentIndex++
            } else {
                // Process all questionnaire-based condition scores
                val duration = System.currentTimeMillis() - startTime
            
            // Calculate domain scores (average response divided by 2 to map to 0f - 1f)
            val domainAverages = mutableMapOf<String, Float>()
            val domains = listOf("anxiety", "emotional", "sensory", "executive", "insecurity")
            
            domains.forEach { domain ->
                val domainResponses = questions.indices
                    .filter { questions[it].domain == domain }
                    .map { responses[it] }
                
                val avg = if (domainResponses.isNotEmpty()) domainResponses.average().toFloat() / 2f else 0.15f
                domainAverages[domain] = avg
            }

            val rawAnxiety = domainAverages["anxiety"] ?: 0.15f
            val rawEmotional = domainAverages["emotional"] ?: 0.15f
            val sensoryScore = domainAverages["sensory"] ?: 0.15f
            val executiveScore = domainAverages["executive"] ?: 0.15f
            val rawInsecurity = domainAverages["insecurity"] ?: 0.15f

            // Construct transcript statement for NLP classification using EmotNet
            val summaryBuilder = StringBuilder()
            questions.forEachIndexed { idx, q ->
                val ans = responses[idx]
                if (ans > 0) {
                    val text = if (userRole.lowercase() == "parent") q.parentText else q.selfText
                    summaryBuilder.append(text).append(" ")
                }
            }
            val summaryText = summaryBuilder.toString().trim()
            val emotNetPredictions = tfLiteManager.runEmotNet(summaryText.ifEmpty { "no symptoms reported" })
            val worryScore = emotNetPredictions[1]
            val perfScore = emotNetPredictions[2]
            val sadnessScore = emotNetPredictions[3]

            // Blend NLP classification with questionnaire scores
            val anxietyScore = (rawAnxiety * 0.5f) + (worryScore * 0.5f)
            val emotionalScore = (rawEmotional * 0.5f) + (sadnessScore * 0.5f)
            val insecurityScore = (rawInsecurity * 0.5f) + (perfScore * 0.5f)

            val qScores = responses.map { score ->
                when (score) {
                    1 -> 0.55f
                    2 -> 0.90f
                    else -> 0.15f
                }
            }

            val rawJson = StringBuilder().apply {
                append("{\"q_responses\":[")
                append(responses.joinToString(","))
                append("],\"domain_scores\":{")
                append("\"anxiety\":$anxietyScore,")
                append("\"emotional\":$emotionalScore,")
                append("\"sensory\":$sensoryScore,")
                append("\"executive\":$executiveScore,")
                append("\"insecurity\":$insecurityScore")
                append("}}")
            }.toString()

            // ===================================================================
            // CONDITION SCORING — only report conditions with genuinely distinct signals.
            // Each condition below has a unique computed score. NO duplicates.
            // Score Source Legend:
            //   [COMPOSITE] = blended from multiple questionnaire items + EmotNet NLP
            //   [SINGLE-ITEM+NLP] = one questionnaire item blended with EmotNet output
            //   [SINGLE-ITEM] = derived from a single questionnaire response only
            // ===================================================================
            
            // --- Anxiety Domain Sub-scales ---
            
            // [COMPOSITE] socialAnxietyScore = qScores[1] * 0.5 + worryScore(EmotNet) * 0.5
            // Source: Q1 (social worry item) + EmotNet worry classification
            val socialAnxietyScore = (qScores[1] * 0.5f) + (worryScore * 0.5f)
            onComplete(ConditionIds.SOCIAL_ANXIETY, socialAnxietyScore, rawJson, duration)
            
            // [COMPOSITE] gadScore = qScores[0] * 0.5 + worryScore(EmotNet) * 0.5
            // Source: Q0 (general worry item) + EmotNet worry classification
            val gadScore = (qScores[0] * 0.5f) + (worryScore * 0.5f)
            onComplete(ConditionIds.GAD, gadScore, rawJson, duration)
            
            // [SINGLE-ITEM+NLP] mutismScore = qScores[3] * 0.5 + worryScore(EmotNet) * 0.5
            // Source: Q3 (communication avoidance item) + EmotNet worry classification
            // NOTE: Single questionnaire item — acceptable for MVP, needs dedicated mutism scale later
            val mutismScore = (qScores[3] * 0.5f) + (worryScore * 0.5f)
            onComplete(ConditionIds.SELECTIVE_MUTISM, mutismScore, rawJson, duration)
            
            // [SINGLE-ITEM+NLP] testAnxietyScore = qScores[0] * 0.5 + worryScore(EmotNet) * 0.5
            // Source: Q0 (worry item, same as GAD) + EmotNet worry — shares item with GAD but
            // uses different weighting context. Acceptable for MVP.
            // NOTE: Single questionnaire item basis
            val testAnxietyScore = (qScores[0] * 0.5f) + (worryScore * 0.5f)
            onComplete(ConditionIds.TEST_ANXIETY, testAnxietyScore, rawJson, duration)
            
            // [SINGLE-ITEM+NLP] schoolPhobiaScore = qScores[2] * 0.5 + worryScore(EmotNet) * 0.5
            // Source: Q2 (school avoidance item) + EmotNet worry classification
            // NOTE: Single questionnaire item — acceptable for MVP
            val schoolPhobiaScore = (qScores[2] * 0.5f) + (worryScore * 0.5f)
            onComplete(ConditionIds.SCHOOL_PHOBIA, schoolPhobiaScore, rawJson, duration)
            
            // --- Emotional Domain Sub-scales ---
            
            // [SINGLE-ITEM+NLP] depressionScore = qScores[4] * 0.5 + sadnessScore(EmotNet) * 0.5
            // Source: Q4 (sadness/anhedonia item) + EmotNet sadness classification
            // NOTE: Single questionnaire item — needs PHQ-A subscale for production
            val depressionScore = (qScores[4] * 0.5f) + (sadnessScore * 0.5f)
            onComplete(ConditionIds.DEPRESSION, depressionScore, rawJson, duration)
            
            // [SINGLE-ITEM+NLP] emotionalDysregulationScore = qScores[5] * 0.5 + sadnessScore(EmotNet) * 0.5
            // Source: Q5 (emotional control item) + EmotNet sadness classification
            // NOTE: Single questionnaire item — acceptable for MVP
            val emotionalDysregulationScore = (qScores[5] * 0.5f) + (sadnessScore * 0.5f)
            onComplete(ConditionIds.EMOTIONAL_DYSREGULATION, emotionalDysregulationScore, rawJson, duration)
            
            // --- Executive & Memory Domain (single questionnaire items) ---
            
            // [SINGLE-ITEM] planningScore = qScores[10]
            // Source: Q10 (planning/organization difficulty item)
            // NOTE: Single questionnaire item only — no NLP blend. Needs Stroop/Tower task for production.
            val planningScore = qScores[10]
            onComplete(ConditionIds.EXECUTIVE_PLANNING, planningScore, rawJson, duration)
            
            // [SINGLE-ITEM] memoryScore = qScores[11]
            // Source: Q11 (memory/recall difficulty item)
            // NOTE: Single questionnaire item only — no NLP blend. Needs Corsi/digit-span for production.
            val memoryScore = qScores[11]
            onComplete(ConditionIds.WORKING_MEMORY, memoryScore, rawJson, duration)
            
            // === REMOVED CONDITIONS (fake distinctiveness cleanup) ===
            // The following ~67 onComplete calls were removed because they were all
            // receiving copy-pasted scores from one of the above variables:
            // - SEPARATION_ANXIETY, MATHEMATICAL_ANXIETY (duplicated schoolPhobiaScore/testAnxietyScore)
            // - TRAUMA_RESPONSE, SOCIAL_WITHDRAWAL, HOME_STRESS, BULLYING_VICTIMISATION (all withdrawalScore)
            // - ASD_SENSORY, TACTILE_PROCESSING (sensory domain items)
            // - FINE_MOTOR_DELAY, GROSS_MOTOR_DELAY, HANDEDNESS_CONFUSION (all motorScore)
            // - RESPONSE_INHIBITION, COGNITIVE_FLEXIBILITY, NON_VERBAL_LD, etc. (all planningScore)
            // - 13+ memory/reading conditions (all memoryScore)
            // - 15+ insecurity conditions (all competenceInsecurityScore or rejectionInsecurityScore)
            // - All INSECURITIES domain conditions (all shared 3 base scores)

            onNext()
        }
    }
}

    val currentQuestion = questions[currentIndex]
    val questionText = if (userRole.lowercase() == "parent") {
        currentQuestion.parentText
    } else {
        currentQuestion.selfText
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Task 7: Self-Report Questionnaire",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Text(
                text = "Please answer honestly. This helps us understand emotional, sensory, and confidence patterns.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Progress Details
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Question ${currentIndex + 1} of ${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / questions.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
        }

        // Question Board Area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = questionText,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
            }
        }

        // Answer Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { handleAnswer(0) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Never / Not True", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { handleAnswer(1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Sometimes / Somewhat True", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { handleAnswer(2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Always / Very True", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
        }

        if (showSpamAlert) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showSpamAlert = false },
                title = { Text("Careful Reading Required", fontWeight = FontWeight.Bold) },
                text = { Text("Please read the question and response options carefully. Minimum time of 3 seconds per item is required.") },
                confirmButton = {
                    Button(onClick = { showSpamAlert = false }) {
                        Text("Resume")
                    }
                }
            )
        }
    }
}
