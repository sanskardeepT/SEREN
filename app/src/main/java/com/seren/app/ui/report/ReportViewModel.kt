package com.seren.app.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seren.app.data.SerenDatabase
import com.seren.app.data.model.ConditionScore
import com.seren.app.data.model.ScreeningSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SerenDatabase.getDatabase(application)
    private val screeningDao = database.screeningDao()

    private val _session = MutableStateFlow<ScreeningSession?>(null)
    val session: StateFlow<ScreeningSession?> = _session.asStateFlow()

    private val _scores = MutableStateFlow<List<ConditionScore>>(emptyList())
    val scores: StateFlow<List<ConditionScore>> = _scores.asStateFlow()

    fun loadSessionData(sessionId: Long) {
        viewModelScope.launch {
            val sessionDetail = screeningDao.getSessionById(sessionId)
            if (sessionDetail != null) {
                _session.value = sessionDetail
                _scores.value = screeningDao.getConditionScoresForSession(sessionId)
            }
        }
    }
}
