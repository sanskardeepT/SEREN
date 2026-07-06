package com.seren.app.ui.consent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seren.app.data.SerenDatabase
import com.seren.app.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConsentViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SerenDatabase.getDatabase(application)
    private val userDao = database.userDao()

    private val _isConsentSaved = MutableStateFlow(false)
    val isConsentSaved: StateFlow<Boolean> = _isConsentSaved.asStateFlow()

    fun saveConsent(displayName: String, ageGroup: String) {
        viewModelScope.launch {
            val profile = UserProfile(
                displayName = displayName.trim().ifEmpty { "User" },
                ageGroup = ageGroup,
                consentGiven = true,
                consentTimestamp = System.currentTimeMillis()
            )
            userDao.insertProfile(profile)
            _isConsentSaved.value = true
        }
    }
}
