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
            .background(Color(0xFF103652)) // Prussian Blue
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
                // Outermost light blue glowing star
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFF60A5FA).copy(alpha = 0.3f),
                    modifier = Modifier.size(130.dp)
                )
                // Middle gold gradient star outline
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B).copy(alpha = 0.6f),
                    modifier = Modifier.size(100.dp)
                )
                // Innermost solid cyan star
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFF22D3EE),
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
                        .background(Color(0xFF2F80ED))
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
                    containerColor = Color(0xFF2F80ED),
                    contentColor = Color.White
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
            .background(Color(0xFF0F172A)) // Dark backdrop overlying sheet
    ) {
        // White Card representing bottom-sheet layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        gradient = Brush.linearGradient(colors = listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))),
                        textColor = Color(0xFF78350F),
                        isSelected = tempRoleSelection == "parent",
                        onClick = { tempRoleSelection = "parent" }
                    )

                    // Card 2: Teen
                    RoleCard(
                        title = "I am a\nTeen",
                        gradient = Brush.linearGradient(colors = listOf(Color(0xFFD8B4FE), Color(0xFFF472B6))),
                        textColor = Color.White,
                        isSelected = tempRoleSelection == "teen",
                        onClick = { tempRoleSelection = "teen" }
                    )

                    // Card 3: Adult
                    RoleCard(
                        title = "I am an\nAdult",
                        gradient = Brush.linearGradient(colors = listOf(Color(0xFF334155), Color(0xFF1E293B))),
                        textColor = Color.White,
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
                            containerColor = if (tempRoleSelection != null) Color(0xFF1E293B) else Color(0xFFE2E8F0),
                            contentColor = Color.White
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
                color = if (isSelected) Color(0xFF3B82F6) else Color.Transparent,
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
    // Exact text from mockup copy (including typos to preserve visual match specs)
    val mockupTermsText = """
        SEREN app eromated mandmodries to use data and use the tools to provided to mtre and resource your content. We use colaborations and tow data usage data vrom protorotional using and user data, and collectratiom to oxensure your data on is incluzed with precenge and encrypted,. You can emure save message and encryptinz your provessional data usags are encrypted to your data encrypted.
        
        SCREENING NOTICE
        SEREN is an initial screening instrument. Positive indications suggest risk factors and do not constitute a clinical diagnosis. Always seek professional advice.
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
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
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
                    color = Color(0xFF475569),
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
                tint = Color(0xFF3B82F6),
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
                    containerColor = Color(0xFF1E293B), // Dark Slate
                    contentColor = Color.White
                )
            ) {
                Text(text = "Accept & Continue", fontWeight = FontWeight.Bold)
            }
        }
    }
}
