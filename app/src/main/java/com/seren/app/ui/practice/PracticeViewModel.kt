package com.seren.app.ui.practice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seren.app.data.ConditionIds
import com.seren.app.data.SerenDatabase
import com.seren.app.data.model.ConditionScore
import com.seren.app.data.model.ScreeningSession
import com.seren.app.data.model.SessionStatus
import com.seren.app.data.model.SessionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class PracticeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SerenDatabase.getDatabase(application)
    private val screeningDao = database.screeningDao()
    private val userDao = database.userDao()

    val latestScores: StateFlow<List<ConditionScore>> = screeningDao.getLatestScreeningScoresFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _streakCount = MutableStateFlow(0)
    val streakCount: StateFlow<Int> = _streakCount.asStateFlow()

    private val _isPracticeSaved = MutableStateFlow(false)
    val isPracticeSaved: StateFlow<Boolean> = _isPracticeSaved.asStateFlow()

    init {
        calculateStreak()
    }

    private fun calculateStreak() {
        viewModelScope.launch {
            val sessions = screeningDao.getAllSessionsFlow().first()
            val practiceSessions = sessions
                .filter { it.sessionType == SessionType.DAILY_PRACTICE && it.status == SessionStatus.COMPLETED }
                .sortedByDescending { it.completedAt }

            if (practiceSessions.isEmpty()) {
                _streakCount.value = 0
                return@launch
            }

            var streak = 0
            val calendar = Calendar.getInstance()
            
            // Start from today
            var targetDay = calendar.get(Calendar.DAY_OF_YEAR)
            var targetYear = calendar.get(Calendar.YEAR)

            for (session in practiceSessions) {
                val compTime = session.completedAt ?: continue
                calendar.timeInMillis = compTime
                val sessionDay = calendar.get(Calendar.DAY_OF_YEAR)
                val sessionYear = calendar.get(Calendar.YEAR)

                // If session is from target day
                if (sessionDay == targetDay && sessionYear == targetYear) {
                    streak++
                    // Move target day to yesterday
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    targetDay = calendar.get(Calendar.DAY_OF_YEAR)
                    targetYear = calendar.get(Calendar.YEAR)
                } else if (sessionDay < targetDay || sessionYear < targetYear) {
                    // Streak broken (gap in days)
                    break
                }
            }
            _streakCount.value = streak
        }
    }

    fun savePracticeSession() {
        viewModelScope.launch {
            val profile = userDao.getUserProfile()
            if (profile != null) {
                val newSession = ScreeningSession(
                    userId = profile.userId,
                    sessionType = SessionType.DAILY_PRACTICE,
                    status = SessionStatus.COMPLETED,
                    completedAt = System.currentTimeMillis()
                )
                screeningDao.insertSession(newSession)
                _isPracticeSaved.value = true
                calculateStreak()
            }
        }
    }
}
