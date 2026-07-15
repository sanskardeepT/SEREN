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

            // --- Condition scoring routing based on sub-trait questions to avoid aliasing ---
            
            // Anxiety Domain Sub-scales
            val socialAnxietyScore = (qScores[1] * 0.5f) + (worryScore * 0.5f)
            val gadScore = (qScores[0] * 0.5f) + (worryScore * 0.5f)
            val mutismScore = (qScores[3] * 0.5f) + (worryScore * 0.5f)
            val testAnxietyScore = (qScores[0] * 0.5f) + (worryScore * 0.5f)
            val schoolPhobiaScore = (qScores[2] * 0.5f) + (worryScore * 0.5f)

            onComplete(ConditionIds.SOCIAL_ANXIETY, socialAnxietyScore, rawJson, duration)
            onComplete(ConditionIds.GAD, gadScore, rawJson, duration)
            onComplete(ConditionIds.SELECTIVE_MUTISM, mutismScore, rawJson, duration)
            onComplete(ConditionIds.TEST_ANXIETY, testAnxietyScore, rawJson, duration)
            onComplete(ConditionIds.SEPARATION_ANXIETY, schoolPhobiaScore, rawJson, duration)
            onComplete(ConditionIds.SCHOOL_PHOBIA, schoolPhobiaScore, rawJson, duration)
            onComplete(ConditionIds.MATHEMATICAL_ANXIETY, testAnxietyScore, rawJson, duration)
            
            // Emotional Domain Sub-scales
            val depressionScore = (qScores[4] * 0.5f) + (sadnessScore * 0.5f)
            val emotionalDysregulationScore = (qScores[5] * 0.5f) + (sadnessScore * 0.5f)
            val withdrawalScore = (qScores[6] * 0.5f) + (sadnessScore * 0.5f)

            onComplete(ConditionIds.DEPRESSION, depressionScore, rawJson, duration)
            onComplete(ConditionIds.EMOTIONAL_DYSREGULATION, emotionalDysregulationScore, rawJson, duration)
            onComplete(ConditionIds.TRAUMA_RESPONSE, withdrawalScore, rawJson, duration)
            onComplete(ConditionIds.SOCIAL_WITHDRAWAL, withdrawalScore, rawJson, duration)
            onComplete(ConditionIds.HOME_STRESS, withdrawalScore, rawJson, duration)
            onComplete(ConditionIds.BULLYING_VICTIMISATION, withdrawalScore, rawJson, duration)
            
            // Sensory & Motor Domain Sub-scales
            val tactileScore = qScores[9]
            val motorScore = qScores[8]
            val generalSensoryScore = (qScores[7] + qScores[9]) / 2f

            onComplete(ConditionIds.ASD_SENSORY, generalSensoryScore, rawJson, duration)
            onComplete(ConditionIds.TACTILE_PROCESSING, tactileScore, rawJson, duration)
            onComplete(ConditionIds.FINE_MOTOR_DELAY, motorScore, rawJson, duration)
            onComplete(ConditionIds.GROSS_MOTOR_DELAY, motorScore, rawJson, duration)
            onComplete(ConditionIds.HANDEDNESS_CONFUSION, motorScore, rawJson, duration)
            
            // Executive & Memory Domain Sub-scales
            val planningScore = qScores[10]
            val memoryScore = qScores[11]

            onComplete(ConditionIds.EXECUTIVE_PLANNING, planningScore, rawJson, duration)
            onComplete(ConditionIds.RESPONSE_INHIBITION, planningScore, rawJson, duration)
            onComplete(ConditionIds.COGNITIVE_FLEXIBILITY, planningScore, rawJson, duration)
            onComplete(ConditionIds.VERBAL_WORKING_MEMORY, memoryScore, rawJson, duration)
            onComplete(ConditionIds.VISUAL_SPATIAL_MEM, memoryScore, rawJson, duration)
            onComplete(ConditionIds.AUDITORY_DISCRIMINATION, memoryScore, rawJson, duration)
            onComplete(ConditionIds.VISUAL_DISCRIMINATION, memoryScore, rawJson, duration)
            onComplete(ConditionIds.NON_VERBAL_LD, planningScore, rawJson, duration)
            onComplete(ConditionIds.FACT_RETRIEVAL, memoryScore, rawJson, duration)
            onComplete(ConditionIds.PLACE_VALUE_CONFUSION, memoryScore, rawJson, duration)
            onComplete(ConditionIds.FRACTION_RATIO_DEFICIT, memoryScore, rawJson, duration)
            onComplete(ConditionIds.READING_COMPREHENSION, memoryScore, rawJson, duration)
            onComplete(ConditionIds.RAN_DEFICIT, memoryScore, rawJson, duration)
            onComplete(ConditionIds.ORTHOGRAPHIC_PROCESSING, memoryScore, rawJson, duration)
            onComplete(ConditionIds.READING_FLUENCY, memoryScore, rawJson, duration)
            onComplete(ConditionIds.SPELLING_DISORDER, memoryScore, rawJson, duration)
            onComplete(ConditionIds.LOW_FRUSTRATION_TOLERANCE, planningScore, rawJson, duration)
            onComplete(ConditionIds.IMPULSIVITY_NON_ADHD, planningScore, rawJson, duration)
            
            // Insecurities & Silent Profiles Domain (Blended with NLP PerfScore where appropriate)
            val competenceInsecurityScore = (qScores[12] * 0.5f) + (perfScore * 0.5f)
            val rejectionInsecurityScore = (qScores[13] * 0.5f) + (perfScore * 0.5f)
            val fawningInsecurityScore = (qScores[14] * 0.5f) + (perfScore * 0.5f)

            onComplete(ConditionIds.TWICE_EXCEPTIONAL, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ASD_SOCIAL, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ASD_COMMUNICATION, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.HIDDEN_SPATIAL, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.HIDDEN_PATTERN, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.KINESTHETIC_INTEL, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.VERBAL_IQ_SUPPRESSED, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.PRAGMATIC_LANGUAGE, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.THEORY_OF_MIND, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.INTROVERSION_SUPPRESSION, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ADULT_DYSCALCULIA, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ADULT_ANOMIA, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ADULT_READING_COMPREHENSION, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ADULT_PROCESSING_SPEED, competenceInsecurityScore, rawJson, duration)
            
            onComplete(ConditionIds.BODY_IMAGE_INSECURITY, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.IMPOSTER_SYNDROME, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.REJECTION_SENSITIVITY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.WORKPLACE_INSECURITY, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.RELATIONSHIP_INSECURITY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.FOMO_ANXIETY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.DIGITAL_INSECURITY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ACADEMIC_TRAUMA, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.STUTTERING_CONFIDENCE_DEFICIT, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.DECISION_PARALYSIS, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.FINANCIAL_INSECURITY, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.LEADERSHIP_AVOIDANCE, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.CAREER_STAGNATION, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.DEEP_ROOTED_SHYNESS, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.EXPRESSION_INSECURITY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.SOCIAL_BELONGING_INSECURITY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.PERFORMANCE_INSECURITY, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ANGER_INSECURITY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.FAMILY_ORIGIN_INSECURITY, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.SELF_CRITICISM, competenceInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.PUBLIC_SPEAKING_PHOBIA, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.PDA, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.ALEXITHYMIA, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.HSP_OVERWHELM, rejectionInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.FAWN_RESPONSE, fawningInsecurityScore, rawJson, duration)
            onComplete(ConditionIds.PERFECTIONISM, competenceInsecurityScore, rawJson, duration)

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
