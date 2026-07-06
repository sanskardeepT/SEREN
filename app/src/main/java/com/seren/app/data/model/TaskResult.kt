package com.seren.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * TaskResult stores the raw result and signal data for an individual task within a session.
 */
@Entity(
    tableName = "task_results",
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
data class TaskResult(
    @PrimaryKey(autoGenerate = true)
    val resultId: Long = 0,
    val sessionId: Long,
    
    /** 
     * The condition this task evaluates. Matches constants in ConditionIds. 
     * String-based for painless future expansion.
     */
    val conditionId: String,
    
    /** The identifier of the specific task (e.g. "handwriting_reversal", "subitizing", "cpt") */
    val taskType: String,
    
    /** Raw features/metrics stored as JSON (e.g. gaze coordinates, stroke coordinates, reaction times) */
    val rawDataJson: String,
    
    /** Normalized score or classification output from the TFLite model, if applicable */
    val score: Float? = null,
    
    val completedAt: Long = System.currentTimeMillis(),
    val durationMs: Long
)
