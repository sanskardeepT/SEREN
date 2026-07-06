package com.seren.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.seren.app.data.dao.ScreeningDao
import com.seren.app.data.dao.UserDao
import com.seren.app.data.model.ConditionScore
import com.seren.app.data.model.ScreeningSession
import com.seren.app.data.model.TaskResult
import com.seren.app.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        ScreeningSession::class,
        TaskResult::class,
        ConditionScore::class
    ],
    version = 1,
    exportSchema = true
)
abstract class SerenDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun screeningDao(): ScreeningDao

    companion object {
        @Volatile
        private var INSTANCE: SerenDatabase? = null

        fun getDatabase(context: Context): SerenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SerenDatabase::class.java,
                    "seren_database"
                )
                .fallbackToDestructiveMigration() // Simple fallback for pre-launch development
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
