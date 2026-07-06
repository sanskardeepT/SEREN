package com.seren.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * ScreeningSession represents a single screening or practice session run by a user.
 */
@Entity(
    tableName = "screening_sessions",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class ScreeningSession(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0,
    val userId: Long,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val sessionType: String = SessionType.SCREENING,
    val status: String = SessionStatus.IN_PROGRESS
)

object SessionType {
    const val SCREENING = "screening"
    const val DAILY_PRACTICE = "daily_practice"
}

object SessionStatus {
    const val IN_PROGRESS = "in_progress"
    const val COMPLETED = "completed"
    const val ABANDONED = "abandoned"
}
