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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seren.app.data.ConditionIds

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

    val handleAnswer = { score: Int ->
        responses.add(score)
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

            val anxietyScore = domainAverages["anxiety"] ?: 0.15f
            val emotionalScore = domainAverages["emotional"] ?: 0.15f
            val sensoryScore = domainAverages["sensory"] ?: 0.15f
            val executiveScore = domainAverages["executive"] ?: 0.15f
            val insecurityScore = domainAverages["insecurity"] ?: 0.15f

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

            // --- Condition scoring routing based on domains ---
            
            // Batch 2 Anxiety Conditions
            onComplete(ConditionIds.SOCIAL_ANXIETY, anxietyScore, rawJson, duration)
            onComplete(ConditionIds.GAD, anxietyScore, rawJson, duration)
            onComplete(ConditionIds.SELECTIVE_MUTISM, anxietyScore, rawJson, duration)
            onComplete(ConditionIds.TEST_ANXIETY, anxietyScore, rawJson, duration)
            onComplete(ConditionIds.SEPARATION_ANXIETY, anxietyScore, rawJson, duration)
            onComplete(ConditionIds.SCHOOL_PHOBIA, anxietyScore, rawJson, duration)
            
            // Batch 2 Emotional / Behavioral
            onComplete(ConditionIds.DEPRESSION, emotionalScore, rawJson, duration)
            onComplete(ConditionIds.EMOTIONAL_DYSREGULATION, emotionalScore, rawJson, duration)
            
            // Batch 2 Attention & Memory
            onComplete(ConditionIds.EXECUTIVE_FUNCTION, executiveScore, rawJson, duration)
            onComplete(ConditionIds.WORKING_MEMORY, executiveScore, rawJson, duration)
            
            // Batch 3 Speech / Language / Learning
            onComplete(ConditionIds.RECEPTIVE_LANGUAGE, executiveScore, rawJson, duration)
            onComplete(ConditionIds.NON_VERBAL_LD, executiveScore, rawJson, duration)
            
            // Batch 4 subtypes
            onComplete(ConditionIds.DYSPRAXIA, sensoryScore, rawJson, duration)
            onComplete(ConditionIds.VMI, sensoryScore, rawJson, duration)
            onComplete(ConditionIds.ADULT_ANOMIA, executiveScore, rawJson, duration)
            onComplete(ConditionIds.ADULT_PROCESSING_SPEED, executiveScore, rawJson, duration)
            onComplete(ConditionIds.READING_FLUENCY_LAG, executiveScore, rawJson, duration)
            onComplete(ConditionIds.ORTHOGRAPHIC_DEFICIT, executiveScore, rawJson, duration)
            onComplete(ConditionIds.RAN_DEFICIT, executiveScore, rawJson, duration)
            onComplete(ConditionIds.SPELLING_DISORDER, executiveScore, rawJson, duration)
            onComplete(ConditionIds.PLACE_VALUE_CONFUSION, executiveScore, rawJson, duration)
            onComplete(ConditionIds.FRACTION_RATIO_DEFICIT, executiveScore, rawJson, duration)
            
            // Batch 5 subtypes
            onComplete(ConditionIds.MASKING, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.HFA_MASKED, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.SOCIAL_COMMUNICATION_DISORDER, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.PRAGMATIC_LANGUAGE, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.THEORY_OF_MIND, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.SENSORY_PROCESSING, sensoryScore, rawJson, duration)
            onComplete(ConditionIds.SENSORY_DEFENSIVENESS, sensoryScore, rawJson, duration)
            onComplete(ConditionIds.VESTIBULAR_DIFFICULTY, sensoryScore, rawJson, duration)
            onComplete(ConditionIds.PROPRIOCEPTIVE_DIFFICULTY, sensoryScore, rawJson, duration)
            onComplete(ConditionIds.TRAUMA_SILENCE, emotionalScore, rawJson, duration)
            
            // Batch 6 Insecurities
            onComplete(ConditionIds.BODY_IMAGE_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.IMPOSTER_SYNDROME, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.REJECTION_SENSITIVITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.WORKPLACE_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.RELATIONSHIP_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.FOMO_ANXIETY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.DIGITAL_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.ACADEMIC_TRAUMA, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.STUTTERING_CONFIDENCE_DEFICIT, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.DECISION_PARALYSIS, insecurityScore, rawJson, duration)
            
            // Batch 7 Insecurities
            onComplete(ConditionIds.FINANCIAL_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.LEADERSHIP_AVOIDANCE, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.CAREER_STAGNATION, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.DEEP_ROOTED_SHYNESS, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.EXPRESSION_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.SOCIAL_BELONGING_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.PERFORMANCE_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.ANGER_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.FAMILY_ORIGIN_INSECURITY, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.SELF_CRITICISM, insecurityScore, rawJson, duration)
            
            // Batch 8 Insecurities & Silent Profiles
            onComplete(ConditionIds.PUBLIC_SPEAKING_PHOBIA, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.PDA, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.ALEXITHYMIA, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.HSP_OVERWHELM, insecurityScore, rawJson, duration)
            onComplete(ConditionIds.FAWN_RESPONSE, insecurityScore, rawJson, duration)

            onNext()
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
    }
}
