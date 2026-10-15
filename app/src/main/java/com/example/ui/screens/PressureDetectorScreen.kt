package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.PhoneLocked
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SecurityUpdateWarning
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertError
import com.example.ui.theme.AlertErrorContainer
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.NavyPrimary
import com.example.viewmodel.PaySaathiViewModel

@Composable
fun PressureDetectorScreen(
    viewModel: PaySaathiViewModel,
    onBack: () -> Unit
) {
    val verificationResult by viewModel.verificationResult.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("pressure_detector_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Back Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Go Back", tint = NavyPrimary)
            }
            Text(
                text = "Transaction Verification Flow",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
            )
        }

        // Top Badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SecurityUpdateWarning,
                    contentDescription = null,
                    tint = NavyPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "AI SHIELD ACTIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AlertErrorContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AlertError)
                    )
                    Text(
                        text = "Severe Risk Detected",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AlertError
                        )
                    )
                }
            }
        }

        // Title and Subtitle
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Pressure Detector",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
            )
            Text(
                text = "Scammers use psychological pressure so you don't have time to think. Let's inspect the caller's tactics.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // Suspicious Call Quote Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AlertErrorContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = AlertError, modifier = Modifier.size(18.dp))
                        }
                        Text(
                            text = "Suspicious Call Quote",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                    }

                    Text(
                        text = "• Recorded",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AlertError
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "“Caller: 'Sir, pay ₹4,850 within 15 minutes or BESCOM power will be cut off immediately! Stay on the line and do not disconnect.'”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Psychological Stress Index Gauge
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Psychological Stress Index",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                        Text(
                            text = "96% High Pressure",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = AlertError)
                        )
                    }

                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.96f)
                                .height(10.dp)
                                .clip(CircleShape)
                                .background(AlertError)
                        )
                    }
                }
            }
        }

        // Section: 4 Psychological Tactics Identified
        Text(
            text = "4 Psychological Tactics Identified",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = NavyPrimary
            )
        )

        // Tactic 1: Urgency
        TacticCard(
            icon = Icons.Default.Timer,
            title = "1. URGENCY",
            subtitle = "Artificial Panic",
            isDetected = true,
            tacticText = "Imposes a fake 15-minute deadline to create panic.",
            guidanceText = "Real utility bills give written notice and grace periods. Hang up."
        )

        // Tactic 2: Authority Impersonation
        TacticCard(
            icon = Icons.Default.Badge,
            title = "2. AUTHORITY IMPERSONATION",
            subtitle = "False Official Status",
            isDetected = true,
            tacticText = "Pretending to be BESCOM Electricity Officer.",
            guidanceText = "Government & utility staff never demand direct UPI transfers over phone calls."
        )

        // Tactic 3: Secrecy / Isolation
        TacticCard(
            icon = Icons.Default.PhoneLocked,
            title = "3. SECRECY / ISOLATION",
            subtitle = "Cutting Off Advice",
            isDetected = true,
            tacticText = "Telling you to stay on call and not consult family.",
            guidanceText = "Always pause and talk to a family member or neighbor before sending money."
        )

        // Tactic 4: Pay-First-Verify-Later
        TacticCard(
            icon = Icons.Default.ReceiptLong,
            title = "4. PAY-FIRST-VERIFY-LATER",
            subtitle = "Hidden Invoices",
            isDetected = false,
            badgeLabel = "CAUTION",
            badgeColor = AmberTertiary,
            tacticText = "Demanding payment before showing official bill record.",
            guidanceText = "Only check official apps or your printed physical utility bill."
        )

        // Critical Scam Pattern Detected Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AlertErrorContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AlertError),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Dangerous, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Critical Scam Pattern Detected",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    )
                    Text(
                        text = "Someone is pressuring you to act quickly and keep the call active. These are classic scam hallmarks.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // Actions
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = AlertError),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("pause_and_verify_button")
        ) {
            Icon(Icons.Default.FrontHand, contentDescription = null, tint = Color.White)
            Spacer(Modifier.size(8.dp))
            Text("Pause and Verify (Do Not Pay)", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Button(
            onClick = {
                viewModel.playAudio(
                    "Psychological Pressure Detected! The caller is using artificial urgency, impersonating BESCOM, and preventing you from calling family. Please disconnect this call immediately.",
                    currentLanguage.code
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("read_aloud_explanation_button")
        ) {
            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = NavyPrimary)
            Spacer(Modifier.size(8.dp))
            Text(
                text = if (isSpeaking) "बोल रहे हैं... (Playing Audio)" else "Read Aloud Explanation (हिन्दी / ಕನ್ನಡ / Eng)",
                color = NavyPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
            Spacer(Modifier.size(4.dp))
            Text(
                text = "PaySaathi AI provides guided safety warnings; always verify through official channels.",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun TacticCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isDetected: Boolean,
    badgeLabel: String = "DETECTED",
    badgeColor: Color = AlertError,
    tacticText: String,
    guidanceText: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isDetected) AlertErrorContainer else Color(0xFFFFDCC3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isDetected) AlertError else AmberTertiary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDetected) AlertError else badgeColor)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isDetected) "DETECTED" else badgeLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = if (isDetected) Icons.Default.Cancel else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isDetected) AlertError else AmberTertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Tactic: $tacticText",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = guidanceText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
