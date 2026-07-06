package com.seren.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seren.app.data.SerenDatabase
import com.seren.app.data.model.ConditionScore
import com.seren.app.data.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SerenDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val screeningDao = database.screeningDao()

    val userProfile: StateFlow<UserProfile?> = userDao.getUserProfileFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val latestScores: StateFlow<List<ConditionScore>> = screeningDao.getLatestScreeningScoresFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
