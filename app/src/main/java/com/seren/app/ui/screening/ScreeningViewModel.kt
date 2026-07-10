package com.seren.app.ui.screening

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seren.app.data.ConditionIds
import com.seren.app.data.SerenDatabase
import com.seren.app.data.model.ConfidenceLevel
import com.seren.app.data.model.ConditionScore
import com.seren.app.data.model.ScreeningSession
import com.seren.app.data.model.SessionStatus
import com.seren.app.data.model.SessionType
import com.seren.app.data.model.TaskResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScreeningViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SerenDatabase.getDatabase(application)
    private val screeningDao = database.screeningDao()
    private val userDao = database.userDao()

    private val _sessionId = MutableStateFlow<Long?>(null)
    val sessionId: StateFlow<Long?> = _sessionId.asStateFlow()

    private val _currentTaskIndex = MutableStateFlow(0)
    val currentTaskIndex: StateFlow<Int> = _currentTaskIndex.asStateFlow()

    private val _isSessionComplete = MutableStateFlow(false)
    val isSessionComplete: StateFlow<Boolean> = _isSessionComplete.asStateFlow()

    /** Order of assessment task screens */
    val tasksList = listOf(
        "Handwriting",
        "Number Sense",
        "Reading Gaze",
        "RAN Phonological",
        "CPT Attention",
        "Speech Fluency"
    )

    fun startSession() {
        viewModelScope.launch {
            val profile = userDao.getUserProfile()
            if (profile != null) {
                // If there's already an active session, reuse it, otherwise create one
                val active = screeningDao.getActiveSession()
                if (active != null) {
                    _sessionId.value = active.sessionId
                } else {
                    val newSession = ScreeningSession(
                        userId = profile.userId,
                        sessionType = SessionType.SCREENING,
                        status = SessionStatus.IN_PROGRESS
                    )
                    _sessionId.value = screeningDao.insertSession(newSession)
                }
                _currentTaskIndex.value = 0
                _isSessionComplete.value = false
            }
        }
    }

    fun submitTaskResult(
        conditionId: String,
        taskType: String,
        rawDataJson: String,
        score: Float,
        durationMs: Long
    ) {
        val currentSessionId = _sessionId.value ?: return
        viewModelScope.launch {
            val taskResult = TaskResult(
                sessionId = currentSessionId,
                conditionId = conditionId,
                taskType = taskType,
                rawDataJson = rawDataJson,
                score = score,
                durationMs = durationMs
            )
            screeningDao.insertTaskResult(taskResult)
        }
    }

    fun nextTask() {
        if (_currentTaskIndex.value < tasksList.size - 1) {
            _currentTaskIndex.value += 1
        } else {
            completeSession()
        }
    }

    private fun completeSession() {
        val currentSessionId = _sessionId.value ?: return
        viewModelScope.launch {
            // Fetch all individual task metrics saved in database during this session
            val results = screeningDao.getTaskResultsForSession(currentSessionId)
            
            // --- FUSIONNET ENSEMBLE SCORING ---
            // Combine active task results into 0-100 scores for all active conditions.
            val mappedScores = ConditionIds.ACTIVE.filter { it != ConditionIds.TWICE_EXCEPTIONAL }.mapNotNull { conditionId ->
                // Gather task scores targeting this specific condition
                val conditionResults = results.filter { it.conditionId == conditionId }
                
                if (conditionResults.isEmpty()) {
                    return@mapNotNull null
                }
                
                // Average scores
                val riskVal = conditionResults.map { it.score ?: 0f }.average().toFloat() * 100f
                val modalitiesCount = conditionResults.map { it.taskType }.distinct().size
                
                // Confidence labels based on count of active modalities (docs/training-protocols.md Section 9.1):
                // 1 modality ➜ LOW confidence
                // 2-3 modalities ➜ MEDIUM confidence
                // 4-6 modalities ➜ HIGH confidence
                val confidence = when {
                    modalitiesCount <= 1 -> ConfidenceLevel.LOW
                    modalitiesCount <= 3 -> ConfidenceLevel.MEDIUM
                    else -> ConfidenceLevel.HIGH
                }

                ConditionScore(
                    sessionId = currentSessionId,
                    conditionId = conditionId,
                    riskScore = riskVal.coerceIn(0f, 100f),
                    confidenceLevel = confidence,
                    modalitiesUsed = modalitiesCount
                )
            }.toMutableList()

            // Calculate Twice-Exceptional (2e) profile dynamically:
            // High cognitive/academic capacity (e.g. low dyscalculia or executive deficit risk)
            // co-occurring with high learning/attention differences (dyslexia or adhd).
            val dyslexiaScore = mappedScores.find { it.conditionId == ConditionIds.DYSLEXIA }?.riskScore ?: 0f
            val adhdScore = maxOf(
                mappedScores.find { it.conditionId == ConditionIds.ADHD_INATTENTIVE }?.riskScore ?: 0f,
                mappedScores.find { it.conditionId == ConditionIds.ADHD_HYPERACTIVE }?.riskScore ?: 0f,
                mappedScores.find { it.conditionId == ConditionIds.ADHD_COMBINED }?.riskScore ?: 0f
            )
            val dyscalculiaScore = mappedScores.find { it.conditionId == ConditionIds.DYSCALCULIA }?.riskScore ?: 0f
            val execScore = mappedScores.find { it.conditionId == ConditionIds.EXECUTIVE_FUNCTION }?.riskScore ?: 0f

            val highCognitiveCapacity = (100f - minOf(dyscalculiaScore, execScore))
            val cooccurringDifference = maxOf(dyslexiaScore, adhdScore)
            val twiceExceptionalScore = (highCognitiveCapacity * cooccurringDifference / 100f).coerceIn(0f, 100f)

            // Extract anxiety / mutism / memory scores for Batch 5 composites
            val socialAnxietyScore = mappedScores.find { it.conditionId == ConditionIds.SOCIAL_ANXIETY }?.riskScore ?: 0f
            val gadScore = mappedScores.find { it.conditionId == ConditionIds.GAD }?.riskScore ?: 0f
            val testAnxietyScore = mappedScores.find { it.conditionId == ConditionIds.TEST_ANXIETY }?.riskScore ?: 0f
            val selectiveMutismScore = mappedScores.find { it.conditionId == ConditionIds.SELECTIVE_MUTISM }?.riskScore ?: 0f
            val workingMemoryScore = mappedScores.find { it.conditionId == ConditionIds.WORKING_MEMORY }?.riskScore ?: 0f

            // Masking: low math/ADHD risk combined with high GAD, social anxiety, or test anxiety
            val maskingScore = ((100f - minOf(dyscalculiaScore, adhdScore)) * maxOf(socialAnxietyScore, gadScore, testAnxietyScore) / 100f).coerceIn(0f, 100f)

            // HFA Masked: high social anxiety/mutism combined with executive/memory deficits
            val hfaMaskedScore = (maxOf(socialAnxietyScore, selectiveMutismScore) * maxOf(execScore, workingMemoryScore) / 100f).coerceIn(0f, 100f)

            mappedScores.add(
                ConditionScore(
                    sessionId = currentSessionId,
                    conditionId = ConditionIds.TWICE_EXCEPTIONAL,
                    riskScore = twiceExceptionalScore,
                    confidenceLevel = ConfidenceLevel.MEDIUM,
                    modalitiesUsed = 2
                )
            )

            mappedScores.add(
                ConditionScore(
                    sessionId = currentSessionId,
                    conditionId = ConditionIds.MASKING,
                    riskScore = maskingScore,
                    confidenceLevel = ConfidenceLevel.MEDIUM,
                    modalitiesUsed = 2
                )
            )

            mappedScores.add(
                ConditionScore(
                    sessionId = currentSessionId,
                    conditionId = ConditionIds.HFA_MASKED,
                    riskScore = hfaMaskedScore,
                    confidenceLevel = ConfidenceLevel.MEDIUM,
                    modalitiesUsed = 2
                )
            )

            // Save to database
            screeningDao.insertConditionScores(mappedScores)
            
            // Mark session as complete
            screeningDao.updateSessionStatus(
                sessionId = currentSessionId,
                completedAt = System.currentTimeMillis(),
                status = SessionStatus.COMPLETED
            )
            
            _isSessionComplete.value = true
        }
    }
}
