package com.seren.app.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.seren.app.R

/**
 * SEREN Typography — Google Fonts
 * Source: docs/business-and-tone.md
 *
 * Headings: Fraunces — elegant, warm serif
 * Body: Inter — clean, highly readable sans-serif
 */

val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fraunces = FontFamily(
    Font(
        googleFont = GoogleFont("Fraunces"),
        fontProvider = fontProvider
    )
)

val inter = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = fontProvider
    )
)
