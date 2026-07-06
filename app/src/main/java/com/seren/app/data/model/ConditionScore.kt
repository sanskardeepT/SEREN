package com.seren.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * ConditionScore stores the fused risk assessment score for a condition in a screening session.
 */
@Entity(
    tableName = "condition_scores",
    foreignKeys = [
        ForeignKey(
            entity = ScreeningSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sessionId"])]
)
data class ConditionScore(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Long = 0,
    val sessionId: Long,
    
    /** The condition evaluated (e.g. "dyslexia", "adhd_combined") */
    val conditionId: String,
    
    /** Risk score ranging from 0.0 to 100.0 */
    val riskScore: Float,
    
    /** Confidence label (LOW / MEDIUM / HIGH) determined by count of active modalities */
    val confidenceLevel: String,
    
    /** Count of independent detection modalities fused for this result */
    val modalitiesUsed: Int,
    
    val computedAt: Long = System.currentTimeMillis()
)

object ConfidenceLevel {
    const val LOW = "low"
    const val MEDIUM = "medium"
    const val HIGH = "high"
}
