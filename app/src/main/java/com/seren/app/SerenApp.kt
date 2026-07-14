package com.seren.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.seren.app.navigation.SerenNavGraph
import com.seren.app.update.UpdateConfig
import com.seren.app.update.UpdateManager

@Composable
fun SerenApp() {
    val navController = rememberNavController()
    var updateConfig by remember { mutableStateOf<UpdateConfig?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val config = UpdateManager.checkForUpdates()
        if (config != null) {
            updateConfig = config
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SerenNavGraph(navController = navController)
        
        updateConfig?.let { config ->
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { 
                    if (!config.forceUpdate) {
                        updateConfig = null
                    }
                },
                title = {
                    Text(
                        text = "New Update Available! 🚀",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Version ${config.latestVersionName} is now ready for download.",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Release Notes:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = config.releaseNotes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        if (config.forceUpdate) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "* This is a mandatory update to maintain access to screening features and models.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            try {
                                val urlToOpen = if (config.downloadUrl.isNotEmpty()) config.downloadUrl else config.playStoreUrl
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(urlToOpen))
                                context.startActivity(intent)
                            } catch (e: Exception) {}
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Update Now", color = Color.White)
                    }
                },
                dismissButton = {
                    if (!config.forceUpdate) {
                        OutlinedButton(onClick = { updateConfig = null }) {
                            Text("Later")
                        }
                    }
                }
            )
        }
    }
}
