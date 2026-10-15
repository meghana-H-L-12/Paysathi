package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.InspectionStep
import com.example.models.PaymentType
import com.example.models.RiskLevel
import com.example.ui.theme.AlertError
import com.example.ui.theme.AlertErrorContainer
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.NavyPrimary
import com.example.viewmodel.PaySaathiViewModel

@Composable
fun PayGuardScreen(
    viewModel: PaySaathiViewModel,
    onOpenPressureDetector: () -> Unit,
    onCallFamily: () -> Unit,
    onAskSaathi: () -> Unit
) {
    val inspectionStep by viewModel.inspectionStep.collectAsState()
    val payeeName by viewModel.payeeName.collectAsState()
    val upiId by viewModel.upiId.collectAsState()
    val amountText by viewModel.amountText.collectAsState()
    val paymentType by viewModel.paymentType.collectAsState()
    val userIntent by viewModel.userIntent.collectAsState()
    val checklistAnswers by viewModel.checklistAnswers.collectAsState()
    val verificationResult by viewModel.verificationResult.collectAsState()
    val isListening by viewModel.speechManager.isListening.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()

    var isManualEntryMode by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val micPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micPulseAnim"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("pay_guard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(Modifier.height(4.dp)) }

        // Reassuring Intro Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = EmeraldSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "PaySaathi Active Pay Guard",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                    }
                }

                Text(
                    text = "Let's check before you pay.",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                )

                Text(
                    text = "A calm 4-step safety shield to inspect every transfer before a single rupee moves.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Stepper Progress Indicator
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
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
                        Text(
                            text = "Inspection Progress",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AlertErrorContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Step 4 of 4: Red Alert",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AlertError
                                )
                            )
                        }
                    }

                    // 4 Step Bars
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(Modifier.weight(1f).height(6.dp).clip(CircleShape).background(NavyPrimary))
                        Box(Modifier.weight(1f).height(6.dp).clip(CircleShape).background(NavyPrimary))
                        Box(Modifier.weight(1f).height(6.dp).clip(CircleShape).background(NavyPrimary))
                        Box(Modifier.weight(1f).height(6.dp).clip(CircleShape).background(AlertError))
                    }

                    // Stepper Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("1. Payee" to true, "2. Intent" to true, "3. Check" to true, "4. Verdict" to false).forEach { (label, isDone) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(if (isDone) NavyPrimary else AlertError),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isDone) "✓" else "!",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDone) NavyPrimary else AlertError
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // STEP 4: Prominent Multi-Tier Safety Verdict Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AlertErrorContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("verdict_banner_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(AlertError),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dangerous,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AlertError)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "HIGH RISK",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Text(
                                    text = "Urgent Warning",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AlertError,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Text(
                                text = "STOP & PAUSE: High Risk of Social Engineering Impersonation Detected",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    // Reassurance Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReportProblem,
                                contentDescription = null,
                                tint = AlertError,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Legitimate electricity boards never call threatening same-night disconnection at 8 PM or demanding immediate UPI transfers to an individual UPI address.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    // Multi-Tier Safety Rating Showcase
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Current Safety Rating:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Low Risk
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(18.dp))
                                    Text("Safe Payee", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                                    Text("Low Risk", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                            }

                            // Hold Up
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, tint = AmberTertiary, modifier = Modifier.size(18.dp))
                                    Text("Hold Up", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                                    Text("Pause & Verify", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                            }

                            // Red Alert (Active)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AlertError)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Dangerous, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Text("Red Alert", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White))
                                    Text("Scam Detected", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.White))
                                }
                            }
                        }
                    }

                    // Action Triggers Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onCallFamily,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("call_family_button")
                        ) {
                            Icon(Icons.Default.ContactPhone, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Text("Call Family")
                        }

                        Button(
                            onClick = {
                                viewModel.playAudio(
                                    "Warning: High Risk of Social Engineering Impersonation Detected! Legitimate electricity boards never call threatening immediate disconnection. Disconnect this call immediately.",
                                    "en"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("read_aloud_verdict_button")
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Text(if (isSpeaking) "Playing..." else "Read Aloud", color = NavyPrimary)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onAskSaathi,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("ask_saathi_dialog_button")
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = AmberTertiary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Text("Ask Saathi", color = NavyPrimary)
                        }

                        Button(
                            onClick = onOpenPressureDetector,
                            colors = ButtonDefaults.buttonColors(containerColor = AlertError),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("view_pressure_detector_button")
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Text("Inspect Tactics", color = Color.White)
                        }
                    }
                }
            }
        }

        // STEP 1: Payee Info Summary Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(NavyPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Text(
                                text = "Payee Details",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isManualEntryMode) "Manual Input" else "Auto-Captured",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    // Scan QR vs Enter Details switcher
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (!isManualEntryMode) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent)
                                .clickable {
                                    isManualEntryMode = false
                                    viewModel.updatePayeeDetails(
                                        "Electricity Support",
                                        "powerbills-urgent@okaxis",
                                        "4850",
                                        PaymentType.COLLECT_REQUEST
                                    )
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                                Text("Scan QR", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isManualEntryMode) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent)
                                .clickable { isManualEntryMode = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.EditNote, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                                Text("Enter Details", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                            }
                        }
                    }

                    if (!isManualEntryMode) {
                        // Display Summary Rows
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            PayeeDetailRow(icon = Icons.Default.Badge, label = "Payee Name", value = payeeName, isValueBold = true)
                            PayeeDetailRow(icon = Icons.Default.AlternateEmail, label = "UPI ID", value = upiId, isError = true)
                            PayeeDetailRow(icon = Icons.Default.Verified, label = "Amount", value = "₹ $amountText", isValueBold = true, isValueLarge = true)
                            PayeeDetailRow(icon = Icons.Default.CallReceived, label = "Payment Type", value = "Collect Request (Debit)", isError = true)
                        }
                    } else {
                        // Editable fields
                        OutlinedTextField(
                            value = payeeName,
                            onValueChange = { viewModel.updatePayeeDetails(it, upiId, amountText, paymentType) },
                            label = { Text("Recipient / Payee Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = upiId,
                            onValueChange = { viewModel.updatePayeeDetails(payeeName, it, amountText, paymentType) },
                            label = { Text("UPI ID / Phone Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { viewModel.updatePayeeDetails(payeeName, upiId, it, paymentType) },
                            label = { Text("Amount (₹)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // STEP 2: Intent in User's Own Words Card
        item {
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(NavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Column {
                            Text(
                                text = "Your Intent in Your Words",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            )
                            Text(
                                text = "बोलकर या लिखकर बताएं",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Text(
                        text = "Why are you making this payment? Tell me in your own words.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    // Big Tactile Microphone Area
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isListening) {
                                    Box(
                                        modifier = Modifier
                                            .size(76.dp)
                                            .scale(micPulse)
                                            .clip(CircleShape)
                                            .background(EmeraldSecondary.copy(alpha = 0.25f))
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(58.dp)
                                        .clip(CircleShape)
                                        .background(if (isListening) AlertError else EmeraldSecondary)
                                        .clickable {
                                            if (isListening) {
                                                viewModel.stopVoiceInput()
                                            } else {
                                                viewModel.startVoiceInputForIntent()
                                            }
                                        }
                                        .testTag("pay_guard_intent_mic_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Hold to speak intent",
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            Text(
                                text = if (isListening) "Listening... बोलिए / ಮಾತನಾಡಿ" else "Hold to speak in Kannada, Hindi, or English",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isListening) EmeraldSecondary else NavyPrimary
                                )
                            )
                            Text(
                                text = "कन्नड़, हिंदी या अंग्रेज़ी में बोलें",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    // Spoken Transcript Box
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "DETECTED VOICE TRANSCRIPT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "\"$userIntent\"",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.runInspectionAnalysis() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("confirm_reason_button")
                    ) {
                        Icon(Icons.Default.ThumbUp, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.size(6.dp))
                        Text("Confirm My Reason", color = NavyPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // STEP 3: Senior-Friendly Safety Checklist
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(NavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("3", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Column {
                            Text(
                                text = "Senior-Friendly Safety Checklist",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            )
                            Text(
                                text = "5 Gentle Safety Questions",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Text(
                        text = "Please tap the best answer. We use these to double check for common tricks.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    val questions = listOf(
                        1 to "Is this a new payee you've never paid before?",
                        2 to "Is this asking you to approve a 'Collect' request?",
                        3 to "Is the amount unusually large for you?",
                        4 to "Are you currently on a phone call with someone guiding you?",
                        5 to "Is someone asking you to act urgently or secretly?"
                    )

                    questions.forEach { (id, question) ->
                        val currentAnswer = checklistAnswers[id] ?: "NOT_SURE"
                        ChecklistQuestionItem(
                            number = id,
                            questionText = question,
                            selectedAnswer = currentAnswer,
                            onAnswerSelected = { ans ->
                                viewModel.answerChecklistQuestion(id, ans)
                            }
                        )
                    }
                }
            }
        }

        // Reassurance Note
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AmberTertiary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "PaySaathi Reassurance",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                        Text(
                            text = "Take a deep breath. Your electricity cannot be cut without a physical written notice delivered days in advance. Disconnect this phone call immediately.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }
        }

        // Core Security Promise
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                Text(
                    text = "PaySaathi Security Promise",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                )
                Text(
                    text = "Never moves money, approves UPI requests, or collects UPI PINs or OTPs.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Composable
fun PayeeDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isValueBold: Boolean = false,
    isValueLarge: Boolean = false,
    isError: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                Text(label, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }

            Text(
                text = value,
                style = if (isValueLarge) MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isError) AlertError else NavyPrimary
                ) else MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isValueBold) FontWeight.Bold else FontWeight.Medium,
                    color = if (isError) AlertError else NavyPrimary
                )
            )
        }
    }
}

@Composable
fun ChecklistQuestionItem(
    number: Int,
    questionText: String,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$number", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                }
                Text(
                    text = questionText,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val isRedAlertTrigger = (number == 4 || number == 5)
                listOf("YES", "NO", "NOT_SURE").forEach { option ->
                    val isChosen = selectedAnswer == option
                    val label = when (option) {
                        "YES" -> if (isRedAlertTrigger) "YES ⚠️" else "YES ✓"
                        "NO" -> "NO"
                        else -> "NOT SURE"
                    }
                    val bg = when {
                        isChosen && isRedAlertTrigger && option == "YES" -> AlertError
                        isChosen -> NavyPrimary
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    }
                    val textColor = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(bg)
                            .clickable { onAnswerSelected(option) }
                            .padding(vertical = 8.dp)
                            .testTag("checklist_${number}_$option"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        )
                    }
                }
            }
        }
    }
}
