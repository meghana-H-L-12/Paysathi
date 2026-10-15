package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SmsFailed
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.AppLanguage
import com.example.ui.theme.AlertError
import com.example.ui.theme.AlertErrorContainer
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.EmeraldSecondaryContainer
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavyPrimaryContainer
import com.example.viewmodel.PaySaathiViewModel
import com.example.viewmodel.VoiceChatMessage

@Composable
fun VoiceAssistantScreen(
    viewModel: PaySaathiViewModel,
    onCallFamily: () -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isListening by viewModel.speechManager.isListening.collectAsState()
    val isSpeaking by viewModel.speechManager.isSpeaking.collectAsState()

    var inputQueryText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val infiniteTransition = rememberInfiniteTransition(label = "voicePulse")
    val micPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAnim"
    )

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("voice_assistant_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(Modifier.height(4.dp)) }

        // Saathi Voice Guardian Status Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SmartToy, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(24.dp))
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldSecondary)
                                )
                            }

                            Column {
                                Text(
                                    text = "Saathi Voice Guardian",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = NavyPrimary
                                    )
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(Modifier.size(6.dp).clip(CircleShape).background(EmeraldSecondary))
                                    Text(
                                        text = "Active • Ready in 3 Languages",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = EmeraldSecondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                if (isSpeaking) viewModel.stopAudio()
                                else viewModel.playAudio("Namaste! Saathi Voice Guardian is active. Speak in Kannada, Hindi, or English.")
                            }
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Speaker status", tint = NavyPrimary)
                        }
                    }

                    // Language Selector Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            AppLanguage.ENGLISH to "English",
                            AppLanguage.HINDI to "हिन्दी",
                            AppLanguage.KANNADA to "ಕನ್ನಡ"
                        ).forEach { (lang, label) ->
                            val isChosen = currentLanguage == lang
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isChosen) NavyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.setLanguage(lang) }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                                    .testTag("voice_lang_${lang.code}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (isChosen) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isChosen) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Context Protection Banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Policy, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                    }

                    Column {
                        Text("Live Context Protection", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        Text(
                            text = "Checking ₹4,850 UPI Collect Request",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                        )
                    }
                }
            }
        }

        // Conversational Stream Items
        items(chatMessages) { message ->
            if (message.isUser) {
                // User voice query bubble
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("You asked via voice", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        Icon(Icons.Default.Mic, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shadowElevation = 1.dp
                    ) {
                        Text(
                            text = "\"${message.text}\"",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = NavyPrimary,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 24.sp
                            ),
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            } else {
                // Saathi AI Safety Guidance Bubble
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(EmeraldSecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = message.badgeTitle ?: "Saathi Safety Shield",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(AlertErrorContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.FrontHand, contentDescription = null, tint = AlertError, modifier = Modifier.size(20.dp))
                                }

                                Text(
                                    text = message.text,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 24.sp
                                    )
                                )
                            }

                            // Tactile Audio Player Bar
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
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(NavyPrimary)
                                                .clickable {
                                                    if (isSpeaking) viewModel.stopAudio()
                                                    else viewModel.playAudio(message.audioScript ?: message.text)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = "Play advice",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Column {
                                            Text("Listen to Advice", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                                            Text("हिन्दी (सुनिए) • ಕನ್ನಡ (ಕೇಳಿ)", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                        }
                                    }

                                    // Simple animated waveform indicators
                                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Box(Modifier.size(3.dp, 10.dp).clip(CircleShape).background(NavyPrimary))
                                        Box(Modifier.size(3.dp, 18.dp).clip(CircleShape).background(NavyPrimary))
                                        Box(Modifier.size(3.dp, 12.dp).clip(CircleShape).background(NavyPrimary))
                                        Box(Modifier.size(3.dp, 20.dp).clip(CircleShape).background(NavyPrimary))
                                        Box(Modifier.size(3.dp, 8.dp).clip(CircleShape).background(NavyPrimary.copy(alpha = 0.5f)))
                                    }
                                }
                            }

                            // Quick Helpful Action Pills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            viewModel.sendUserVoiceOrTextMessage("Can you explain that more simply?")
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                                        Text("Explain Simply", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = NavyPrimary))
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(AlertErrorContainer)
                                        .clickable { onCallFamily() }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Icon(Icons.Default.Call, contentDescription = null, tint = AlertError, modifier = Modifier.size(14.dp))
                                        Text("Call Family", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = AlertError, fontWeight = FontWeight.Bold))
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            viewModel.playAudio(message.audioScript ?: message.text)
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Icon(Icons.Default.Replay, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                                        Text("Repeat", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Suggested Voice Queries Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Tap to ask quickly:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .clickable { viewModel.sendUserVoiceOrTextMessage("Is this SMS a scam?") }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.SmsFailed, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                            Text("Is this SMS a scam?", style = MaterialTheme.typography.labelSmall.copy(color = NavyPrimary))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .clickable { viewModel.sendUserVoiceOrTextMessage("What is a UPI collect request?") }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.HelpCenter, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                            Text("What is a collect request?", style = MaterialTheme.typography.labelSmall.copy(color = NavyPrimary))
                        }
                    }
                }
            }
        }

        // Giant Interactive Voice & Text Controller Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isListening) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .scale(micPulse)
                                    .clip(CircleShape)
                                    .background(EmeraldSecondary.copy(alpha = 0.25f))
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(if (isListening) EmeraldSecondary else NavyPrimary)
                                .clickable {
                                    if (isListening) {
                                        viewModel.stopVoiceInput()
                                    } else {
                                        viewModel.speechManager.startListening(currentLanguage.code) { query ->
                                            viewModel.sendUserVoiceOrTextMessage(query)
                                        }
                                    }
                                }
                                .testTag("voice_assistant_giant_mic_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = "Tap to speak", tint = Color.White, modifier = Modifier.size(36.dp))
                                Text(
                                    text = if (isListening) "Listening..." else "Tap to Speak",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD6E3FF)
                                    )
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isListening) "बोलिए, हम सुन रहे हैं..." else currentLanguage.voicePrompt,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isListening) EmeraldSecondary else NavyPrimary
                            )
                        )
                        Text(
                            text = "Saathi understands voice questions in Hindi, Kannada & English",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    // Alternative Text Input Field
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputQueryText,
                            onValueChange = { inputQueryText = it },
                            placeholder = { Text("Or type your question here...", fontSize = 13.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("voice_text_input_field"),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                if (inputQueryText.isNotBlank()) {
                                    viewModel.sendUserVoiceOrTextMessage(inputQueryText)
                                    inputQueryText = ""
                                }
                            },
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NavyPrimary)
                                .testTag("voice_text_send_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Safety & Ethics Disclaimer at bottom
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Saathi AI Safety & Ethics Guarantee", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                        Text(
                            text = "Saathi provides guidance based on RBI & National Cyber Crime Reporting guidelines. Saathi never executes transfers, reads private chats, or asks for your confidential UPI PIN/OTP.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
