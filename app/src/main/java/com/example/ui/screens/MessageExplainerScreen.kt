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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.GppMaybe
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Troubleshoot
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.AppLanguage
import com.example.models.RiskLevel
import com.example.ui.theme.AlertError
import com.example.ui.theme.AlertErrorContainer
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.EmeraldSecondaryContainer
import com.example.ui.theme.NavyPrimary
import com.example.viewmodel.PaySaathiViewModel

@Composable
fun MessageExplainerScreen(
    viewModel: PaySaathiViewModel,
    onAskSaathi: () -> Unit
) {
    val messageInput by viewModel.messageInput.collectAsState()
    val activeTab by viewModel.messageExplainerTab.collectAsState()
    val messageAnalysis by viewModel.messageAnalysis.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    var showSimpleExplanation by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("message_explainer_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(Modifier.height(4.dp)) }

        // Warm Mascot Safety Intro
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PsychologyAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Message Explainer",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                        Text(
                            text = "Paste or share any confusing SMS, WhatsApp alert, or bank notice. We translate technical banking words into plain, safe language.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(EmeraldSecondaryContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(14.dp))
                                Text(
                                    text = "100% Private & Offline Checked",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Mode Tabs: Paste Text, Share Message, Screenshot
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(4.dp)
            ) {
                listOf(
                    Triple("paste", "Paste Text", Icons.Default.ContentPaste),
                    Triple("share", "Share Message", Icons.Default.Share),
                    Triple("screenshot", "Screenshot", Icons.Default.PhotoCamera)
                ).forEach { (tabKey, tabLabel, tabIcon) ->
                    val isChosen = activeTab == tabKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isChosen) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent)
                            .clickable {
                                viewModel.setMessageExplainerTab(tabKey)
                                if (tabKey == "screenshot") {
                                    viewModel.updateMessageInput("Dear Customer, Your SBI account KYC has expired. Download the attached APK file or update details immediately to prevent account suspension.")
                                    viewModel.explainMessage("Dear Customer, Your SBI account KYC has expired. Download the attached APK file or update details immediately to prevent account suspension.")
                                } else if (tabKey == "paste" && messageInput.isEmpty()) {
                                    viewModel.updateMessageInput("Dear customer, your SBI NetBanking rewards of Rs 9,850 are expiring today! Click http://sbi-rewards-claim.xyz/redeem to update your PAN & collect cash directly into your account.")
                                    viewModel.explainMessage("Dear customer, your SBI NetBanking rewards of Rs 9,850 are expiring today! Click http://sbi-rewards-claim.xyz/redeem to update your PAN & collect cash directly into your account.")
                                }
                            }
                            .padding(vertical = 8.dp)
                            .testTag("tab_$tabKey"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(tabIcon, contentDescription = null, tint = if (isChosen) NavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            Text(
                                text = tabLabel,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isChosen) NavyPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }

        // Input Card
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.ChatBubble, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                            Text(
                                text = "Confusing Message Content",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .clickable { viewModel.clearMessageInput() }
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(Icons.Default.ClearAll, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Clear",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { viewModel.updateMessageInput(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("message_input_field"),
                        placeholder = { Text("Paste bank SMS or link here...") }
                    )

                    // Screenshot browse tile
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Have a screenshot instead?", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                                    Text("Tap to simulate OCR analysis", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    .clickable {
                                        viewModel.updateMessageInput("ALERT: Electricity power cut scheduled tonight! Transfer pending balance to 9876543210@paytm immediately.")
                                        viewModel.explainMessage("ALERT: Electricity power cut scheduled tonight! Transfer pending balance to 9876543210@paytm immediately.")
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Sample", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                            }
                        }
                    }

                    // Big Explain Button
                    Button(
                        onClick = { viewModel.explainMessage(messageInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("explain_message_button")
                    ) {
                        Icon(Icons.Default.Troubleshoot, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.size(8.dp))
                        Text("Explain This Message Plainly", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.size(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // RESULT SECTION
        if (messageAnalysis != null) {
            val analysis = messageAnalysis!!

            // 1. Safety Verdict Header Banner
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertError else EmeraldSecondary,
                    shadowElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertErrorContainer else EmeraldSecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (analysis.riskLevel == RiskLevel.RED_ALERT) Icons.Default.GppMaybe else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertError else EmeraldSecondary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertErrorContainer else EmeraldSecondaryContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (analysis.riskLevel == RiskLevel.RED_ALERT) "HIGH RISK" else "SAFE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertError else EmeraldSecondary
                                    )
                                )
                            }
                            Text(
                                text = analysis.verdictTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = analysis.verdictSubtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
            }

            // 2. Category Badges
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Detected Message Category",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertErrorContainer else EmeraldSecondaryContainer)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (analysis.riskLevel == RiskLevel.RED_ALERT) Icons.Default.Dangerous else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertError else EmeraldSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = analysis.category.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (analysis.riskLevel == RiskLevel.RED_ALERT) AlertError else EmeraldSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Plain Meaning / आसान भाषा में
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shadowElevation = 1.dp,
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
                                        .background(NavyPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Translate, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                                Text(
                                    text = "Plain Meaning / आसान भाषा में",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = NavyPrimary
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        viewModel.playAudio(
                                            if (currentLanguage == AppLanguage.HINDI) analysis.plainMeaningHindi else analysis.plainMeaningEnglish,
                                            currentLanguage.code
                                        )
                                    }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                                Text(
                                    text = if (isSpeaking) "Playing..." else "Audio",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.HINDI -> analysis.plainMeaningHindi
                                        AppLanguage.KANNADA -> analysis.plainMeaningKannada
                                        AppLanguage.ENGLISH -> analysis.plainMeaningEnglish
                                    },
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 26.sp
                                    )
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.ThumbUp, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = "Banks will never ask you to update PAN on an external third-party website.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Warning Signs Bento
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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.CrisisAlert, contentDescription = null, tint = AlertError, modifier = Modifier.size(20.dp))
                                Text(
                                    text = "${analysis.warningSigns.size} Warning Signs PaySaathi Found",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = NavyPrimary
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(AlertErrorContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${analysis.warningSigns.size} / ${analysis.warningSigns.size} Traps",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = AlertError)
                                )
                            }
                        }

                        analysis.warningSigns.forEach { sign ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(AlertErrorContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.LinkOff, contentDescription = null, tint = AlertError, modifier = Modifier.size(18.dp))
                                    }
                                    Column {
                                        Text(sign.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                                        Text(sign.description, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. What Should You Do Now?
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 1.dp,
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
                            Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(22.dp))
                            Text(
                                text = "What Should You Do Now?",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            )
                        }

                        listOf(
                            Icons.Default.Block to "Do NOT click the web link or open it in a browser.",
                            Icons.Default.Lock to "Never share OTP, PIN, or PAN card details.",
                            Icons.Default.Delete to "Delete the SMS and block the sending phone number."
                        ).forEach { (actionIcon, actionText) ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(AlertErrorContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(actionIcon, contentDescription = null, tint = AlertError, modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = actionText,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.playAudio(
                                    if (currentLanguage == AppLanguage.HINDI) analysis.plainMeaningHindi else analysis.plainMeaningEnglish,
                                    currentLanguage.code
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Column {
                                Text("Read Aloud", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("बोलकर सुनाएं", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                            }
                        }

                        Button(
                            onClick = { showSimpleExplanation = !showSimpleExplanation },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = AmberTertiary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Column {
                                Text("Explain Simpler", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("और सरल रूप", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.clearMessageInput()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Autorenew, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Text("Check Another", color = NavyPrimary)
                        }

                        Button(
                            onClick = onAskSaathi,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(4.dp))
                            Text("Ask Saathi AI", color = Color.White)
                        }
                    }
                }
            }
        }

        // Privacy note at bottom
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "PaySaathi operates in zero-execution mode. We never open external URLs or store private data.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
