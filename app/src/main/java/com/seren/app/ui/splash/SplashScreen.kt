package com.seren.app.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.seren.app.data.SerenDatabase
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToConsent: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { SerenDatabase.getDatabase(context) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        // Fade in animation
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(1200)
        )
        
        // Wait a brief moment
        delay(800)
        
        // Check database for existing consent profile
        val profile = database.userDao().getUserProfile()
        if (profile != null && profile.consentGiven) {
            onNavigateToHome()
        } else {
            onNavigateToConsent()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .alpha(alpha.value)
        ) {
            Text(
                text = "SEREN",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Every child is a star.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Every adult deserves a second chance.",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
