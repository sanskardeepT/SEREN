package com.seren.app.update

import android.content.Context
import android.util.Log
import com.seren.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class UpdateConfig(
    val latestVersionCode: Int,
    val latestVersionName: String,
    val downloadUrl: String,
    val playStoreUrl: String,
    val forceUpdate: Boolean,
    val releaseNotes: String
)

object UpdateManager {
    private const val TAG = "UpdateManager"
    private const val UPDATE_JSON_URL = "https://getseren.vercel.app/update.json"

    suspend fun checkForUpdates(): UpdateConfig? = withContext(Dispatchers.IO) {
        try {
            val url = URL(UPDATE_JSON_URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val jsonString = reader.use { it.readText() }
                connection.disconnect()
                
                val latestVersionCode = getValueFromJson(jsonString, "latestVersionCode")?.toIntOrNull() ?: 0
                val latestVersionName = getValueFromJson(jsonString, "latestVersionName") ?: ""
                val downloadUrl = getValueFromJson(jsonString, "downloadUrl") ?: ""
                val playStoreUrl = getValueFromJson(jsonString, "playStoreUrl") ?: ""
                val forceUpdate = getValueFromJson(jsonString, "forceUpdate")?.toBoolean() ?: false
                val releaseNotes = getValueFromJson(jsonString, "releaseNotes") ?: ""
                
                val currentVersionCode = BuildConfig.VERSION_CODE
                
                if (latestVersionCode > currentVersionCode) {
                    return@withContext UpdateConfig(
                        latestVersionCode = latestVersionCode,
                        latestVersionName = latestVersionName,
                        downloadUrl = downloadUrl,
                        playStoreUrl = playStoreUrl,
                        forceUpdate = forceUpdate,
                        releaseNotes = releaseNotes
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Update check failed: ${e.message}")
        }
        return@withContext null
    }

    private fun getValueFromJson(json: String, key: String): String? {
        val stringPattern = "\"$key\"\\s*:\\s*\"([^\"]*)\"".toRegex()
        val stringMatch = stringPattern.find(json)
        if (stringMatch != null) {
            return stringMatch.groupValues[1]
        }
        val primitivePattern = "\"$key\"\\s*:\\s*([0-9a-zA-Z\\.\\-_]+)".toRegex()
        val primitiveMatch = primitivePattern.find(json)
        return primitiveMatch?.groupValues?.get(1)?.trim()
    }
}
