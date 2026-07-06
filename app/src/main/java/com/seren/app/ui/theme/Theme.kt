package com.seren.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

/**
 * SEREN Material 3 Theme
 *
 * Dark Navy primary, Amber secondary, Sage tertiary.
 * Fraunces for display/headline, Inter for everything else.
 * Star motif and warm supportive tone throughout.
 */

private val DarkColorScheme = darkColorScheme(
    primary = Navy80,
    onPrimary = Navy10,
    primaryContainer = Navy30,
    onPrimaryContainer = Navy90,

    secondary = Amber80,
    onSecondary = Amber10,
    secondaryContainer = Amber30,
    onSecondaryContainer = Amber90,

    tertiary = Sage80,
    onTertiary = Sage10,
    tertiaryContainer = Sage30,
    onTertiaryContainer = Sage90,

    background = Navy10,
    onBackground = Navy95,
    surface = Navy10,
    onSurface = Navy95,
    surfaceVariant = Neutral20,
    onSurfaceVariant = Neutral80,

    error = Error80,
    onError = Error10,
    errorContainer = Error30,
    onErrorContainer = Error90,
)

private val LightColorScheme = lightColorScheme(
    primary = Navy40,
    onPrimary = Navy99,
    primaryContainer = Navy90,
    onPrimaryContainer = Navy10,

    secondary = Amber40,
    onSecondary = Amber99,
    secondaryContainer = Amber90,
    onSecondaryContainer = Amber10,

    tertiary = Sage40,
    onTertiary = Sage99,
    tertiaryContainer = Sage90,
    onTertiaryContainer = Sage10,

    background = Navy99,
    onBackground = Navy10,
    surface = Navy99,
    onSurface = Navy10,
    surfaceVariant = Neutral95,
    onSurfaceVariant = Neutral30,

    error = Error40,
    onError = Neutral99,
    errorContainer = Error90,
    onErrorContainer = Error10,
)

private val SerenTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = fraunces,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = fraunces,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fraunces,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fraunces,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)

@Composable
fun SerenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SerenTypography,
        content = content
    )
}
