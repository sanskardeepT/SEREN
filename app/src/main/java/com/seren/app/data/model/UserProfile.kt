package com.seren.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User profile — stores consent status and basic demographic info.
 *
 * One profile per user (the app is single-user for B2C individual use).
 * Consent must be given before any screening data is collected.
 */
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,

    val displayName: String = "",

    /** Age group determines task difficulty and normative benchmarks */
    val ageGroup: String = AgeGroup.CHILD_9_12,

    /** Mandatory consent — must be true before any data collection */
    val consentGiven: Boolean = false,

    /** Timestamp when consent was explicitly given */
    val consentTimestamp: Long? = null,

    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Age groups — stored as strings for extensibility.
 * Task difficulty and normative z-score benchmarks are keyed to these bands.
 */
object AgeGroup {
    const val CHILD_5_8 = "child_5_8"
    const val CHILD_9_12 = "child_9_12"
    const val TEEN_13_19 = "teen_13_19"
    const val ADULT_20_PLUS = "adult_20_plus"

    val ALL = listOf(CHILD_5_8, CHILD_9_12, TEEN_13_19, ADULT_20_PLUS)

    fun displayLabel(ageGroup: String): String = when (ageGroup) {
        CHILD_5_8 -> "Child (5–8)"
        CHILD_9_12 -> "Child (9–12)"
        TEEN_13_19 -> "Teen (13–19)"
        ADULT_20_PLUS -> "Adult (20+)"
        else -> ageGroup
    }
}
