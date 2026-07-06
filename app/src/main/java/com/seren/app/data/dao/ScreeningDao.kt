package com.seren.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.seren.app.data.model.ConditionScore
import com.seren.app.data.model.ScreeningSession
import com.seren.app.data.model.TaskResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ScreeningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ScreeningSession): Long

    @Query("UPDATE screening_sessions SET completedAt = :completedAt, status = :status WHERE sessionId = :sessionId")
    suspend fun updateSessionStatus(sessionId: Long, completedAt: Long?, status: String)

    @Query("SELECT * FROM screening_sessions ORDER BY startedAt DESC")
    fun getAllSessionsFlow(): Flow<List<ScreeningSession>>

    @Query("SELECT * FROM screening_sessions WHERE sessionId = :sessionId")
    suspend fun getSessionById(sessionId: Long): ScreeningSession?

    @Query("SELECT * FROM screening_sessions WHERE status = 'in_progress' LIMIT 1")
    suspend fun getActiveSession(): ScreeningSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskResult(result: TaskResult): Long

    @Query("SELECT * FROM task_results WHERE sessionId = :sessionId")
    suspend fun getTaskResultsForSession(sessionId: Long): List<TaskResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConditionScore(score: ConditionScore): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConditionScores(scores: List<ConditionScore>)

    @Query("SELECT * FROM condition_scores WHERE sessionId = :sessionId")
    fun getConditionScoresForSessionFlow(sessionId: Long): Flow<List<ConditionScore>>

    @Query("SELECT * FROM condition_scores WHERE sessionId = :sessionId")
    suspend fun getConditionScoresForSession(sessionId: Long): List<ConditionScore>

    /** Fetch the latest scores computed for the user to display on the home or report screens */
    @Query("""
        SELECT cs.* FROM condition_scores cs
        INNER JOIN (
            SELECT sessionId FROM screening_sessions 
            WHERE status = 'completed' AND sessionType = 'screening'
            ORDER BY completedAt DESC LIMIT 1
        ) latest_sess ON cs.sessionId = latest_sess.sessionId
    """)
    fun getLatestScreeningScoresFlow(): Flow<List<ConditionScore>>
}
