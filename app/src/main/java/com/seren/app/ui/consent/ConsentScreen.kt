package com.seren.app.ui.consent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.painterResource
import com.seren.app.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seren.app.data.model.AgeGroup

@Composable
fun ConsentScreen(
    onNavigateToHome: () -> Unit,
    viewModel: ConsentViewModel = viewModel()
) {
    val isConsentSaved by viewModel.isConsentSaved.collectAsState()
    var currentStep by remember { mutableStateOf(1) } // 1: Splash/Welcome, 2: Role Selection, 3: Consent/Privacy
    var selectedRole by remember { mutableStateOf<String?>(null) } // "parent", "teen", "adult"

    LaunchedEffect(isConsentSaved) {
        if (isConsentSaved) {
            onNavigateToHome()
        }
    }

    when (currentStep) {
        1 -> WelcomeSlide(onNext = { currentStep = 2 })
        2 -> RoleSelectorScreen(
            onBack = { currentStep = 1 },
            onSelect = { role ->
                selectedRole = role
                currentStep = 3
            }
        )
        3 -> ConsentPrivacyScreen(
            onBack = { currentStep = 2 },
            onAccept = {
                val ageBand = when (selectedRole) {
                    "parent" -> AgeGroup.CHILD_9_12
                    "teen" -> AgeGroup.TEEN_13_19
                    else -> AgeGroup.ADULT_20_PLUS
                }
                val name = when (selectedRole) {
                    "parent" -> "Parent Explorer"
                    "teen" -> "Teen Explorer"
                    else -> "Adult Explorer"
                }
                viewModel.saveConsent(name, ageBand)
            }
        )
    }
}

/**
 * Slide 1: STITCH Welcome / Splash Carousel
 */
@Composable
fun WelcomeSlide(onNext: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer) // Prussian Blue replacement
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Nested Glowing Star Motif
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(160.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            ) {
                // Outermost glowing star
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.size(130.dp)
                )
                // Middle star outline
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(100.dp)
                )
                // Innermost solid star
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Branding Text Quote
            Text(
                text = "Every child is a star.\nEvery adult deserves a\nsecond chance.",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(56.dp))

            // Carousel indicator dots: leftmost elongated blue pill
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 24.dp, height = 8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.5f))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.5f))
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Next button
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Next", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

/**
 * Slide 2: STITCH Onboarding Role Selector ("Who is this for?")
 */
@Composable
fun RoleSelectorScreen(
    onBack: () -> Unit,
    onSelect: (String) -> Unit
) {
    var tempRoleSelection by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // White Card representing bottom-sheet layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Logo Ribbon header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.seren_logo),
                        contentDescription = "SEREN Logo",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SEREN",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Text(
                    text = "Who is this for?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // 3 Card Selector layout (Parent, Teen, Adult)
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card 1: Parent
                    RoleCard(
                        title = "I am a\nParent",
                        gradient = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer)),
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        isSelected = tempRoleSelection == "parent",
                        onClick = { tempRoleSelection = "parent" }
                    )

                    // Card 2: Teen
                    RoleCard(
                        title = "I am a\nTeen",
                        gradient = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary)),
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        isSelected = tempRoleSelection == "teen",
                        onClick = { tempRoleSelection = "teen" }
                    )

                    // Card 3: Adult
                    RoleCard(
                        title = "I am an\nAdult",
                        gradient = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.inverseSurface)),
                        textColor = MaterialTheme.colorScheme.inverseOnSurface,
                        isSelected = tempRoleSelection == "adult",
                        onClick = { tempRoleSelection = "adult" }
                    )
                }

                // Buttons controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                    ) {
                        Text(text = "Back", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { tempRoleSelection?.let { onSelect(it) } },
                        enabled = tempRoleSelection != null,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tempRoleSelection != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Continue", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    gradient: Brush,
    textColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = textColor.copy(alpha = 0.15f),
                modifier = Modifier.size(64.dp).align(Alignment.CenterEnd)
            )
        }
    }
}

/**
 * Slide 3: STITCH Consent & Privacy Screen
 */
@Composable
fun ConsentPrivacyScreen(
    onBack: () -> Unit,
    onAccept: () -> Unit
) {
    val mockupTermsText = """
        IMPORTANT INFORMATION AND CONSENT (DPDP ACT COMPLIANT)
        
        1. Purpose of Screening
        SEREN is designed to screen for early indicators of cognitive differences, including learning and language processing profiles. 
        
        2. Clinical Disclaimer
        This application is an initial screening instrument. Positive indications suggest potential risk factors and areas that may benefit from support; they DO NOT constitute a clinical, medical, or psychological diagnosis. Always seek advice from qualified clinicians or medical professionals for a formal evaluation.
        
        3. 100% On-Device Processing & Privacy Policy
        To comply with the Digital Personal Data Protection (DPDP) Act of India and guarantee complete privacy, all sensitive data—including voice recordings, drawing inputs, touch statistics, and task results—are processed and stored 100% on-device. No audio recordings, drawings, or raw interaction logs are ever uploaded to external servers, cloud services, or third parties.
        
        4. Consent Affirmation
        By tapping "Accept", you provide explicit, voluntary, and informed consent for this application to access necessary on-device sensors (such as the microphone for voice analysis and the touchscreen for drawing/interaction analysis) and process this information locally on your device in accordance with the terms stated above. You retain the right to withdraw this consent at any time by uninstalling the application.
    """.trimIndent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Consent & Privacy",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        // Scrollable Terms Area
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = mockupTermsText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }
        }

        // Encryption Indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Data Encrypted Lock",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = "Data Encrypted",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Navigation Button Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Text(text = "Back", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1.5f).height(56.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Accept & Continue", fontWeight = FontWeight.Bold)
            }
        }
    }
}
