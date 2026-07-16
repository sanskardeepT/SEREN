package com.seren.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.seren.app.ui.practice.PracticeViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(
    onNavigateToScreening: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToReport: (sessionId: Long) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val latestScores by viewModel.latestScores.collectAsState()
    var showSupportDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val ageGroup = userProfile?.ageGroup ?: AgeGroup.CHILD_9_12

    if (showSupportDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = {
                Text(
                    text = "SEREN — Terms & Support",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "DPDP Act Compliant Screening",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "To guarantee absolute data privacy, all speech inputs, handwritten coordinates, and attention response scores are processed 100% on-device. No personal identifiers or biometric files are ever transmitted to external servers.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Clinical Disclaimer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "This app is an early screening aid, not a medical diagnostic tool. Please consult with a qualified pediatrician or psychologist for formal developmental evaluations.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Contact Founder & DPO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Name: Sanskardeep Talikote\nPhone: +91 94039 10943\nEmail: sanskardeepbtalikote19@gmail.com",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:sanskardeepbtalikote19@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "SEREN Support Inquiry")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {}
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Email DPO", fontSize = 11.sp)
                    }
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:+919403910943")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {}
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Call Support", fontSize = 11.sp)
                    }
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showSupportDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        )
    }

    when (ageGroup) {
        AgeGroup.CHILD_5_8, AgeGroup.CHILD_9_12 -> ChildDashboard(
            onNavigateToPlay = onNavigateToScreening,
            onNavigateToPractice = onNavigateToPractice,
            onShowSupport = { showSupportDialog = true }
        )
        AgeGroup.TEEN_13_19 -> TeenDashboard(
            onNavigateToChallenge = onNavigateToScreening,
            onNavigateToPractice = onNavigateToPractice,
            onShowSupport = { showSupportDialog = true }
        )
        else -> AdultDashboard(
            onNavigateToScreening = onNavigateToScreening,
            onNavigateToPractice = onNavigateToPractice,
            latestScores = latestScores,
            onNavigateToReport = onNavigateToReport,
            onShowSupport = { showSupportDialog = true }
        )
    }
}
