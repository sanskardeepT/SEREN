package com.seren.app.data

import android.content.Context
import android.content.pm.ApplicationInfo
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
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    SerenDatabase::class.java,
                    "seren_database"
                )
                
                // Securely encrypt database with SQLCipher SupportFactory
                val passphraseBytes = com.seren.app.data.security.SecurityHelper.getOrCreateDatabasePassphrase(context)
                val factory = net.sqlcipher.database.SupportFactory(passphraseBytes)
                builder.openHelperFactory(factory)

                // Only allow destructive migration in debug builds.
                // In release, a missing migration will crash — forcing us to write proper migrations.
                val isDebug = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
                if (isDebug) {
                    builder.fallbackToDestructiveMigration()
                }
                val instance = builder.build()
                INSTANCE = instance
                instance
            }
        }
    }
}
