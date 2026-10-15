package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.models.AppLanguage
import com.example.ui.components.CallFamilyDialog
import com.example.ui.components.FloatingAskSaathiButton
import com.example.ui.components.PaySaathiBottomNav
import com.example.ui.components.PaySaathiTopBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MessageExplainerScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PayGuardScreen
import com.example.ui.screens.PressureDetectorScreen
import com.example.ui.screens.SafetyRecoveryScreen
import com.example.ui.screens.ScamGymScreen
import com.example.ui.screens.VoiceAssistantScreen
import com.example.ui.theme.PaySaathiTheme
import com.example.viewmodel.PaySaathiViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val paySaathiViewModel: PaySaathiViewModel = viewModel()
            val isHighContrast by paySaathiViewModel.isHighContrast.collectAsState()
            val textSizeScale by paySaathiViewModel.textSizeScale.collectAsState()

            val fontScaleFactor = when (textSizeScale) {
                "large" -> 1.15f
                "xlarge" -> 1.30f
                else -> 1.0f
            }

            val currentDensity = LocalDensity.current
            val customDensity = remember(currentDensity, fontScaleFactor) {
                Density(
                    density = currentDensity.density,
                    fontScale = currentDensity.fontScale * fontScaleFactor
                )
            }

            CompositionLocalProvider(LocalDensity provides customDensity) {
                PaySaathiTheme(darkTheme = isSystemInDarkTheme() || isHighContrast) {
                    PaySaathiApp(viewModel = paySaathiViewModel)
                }
            }
        }
    }
}

@Composable
fun PaySaathiApp(viewModel: PaySaathiViewModel) {
    val context = LocalContext.current
    val currentRoute by viewModel.currentRoute.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val textSizeScale by viewModel.textSizeScale.collectAsState()
    val isOnboardingDone by viewModel.isOnboardingDone.collectAsState()

    var showFamilyDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    // If onboarding is not completed, show the Onboarding screen
    if (!isOnboardingDone) {
        OnboardingScreen(
            viewModel = viewModel,
            onContinue = {
                viewModel.setRoute("home")
            }
        )
        return
    }

    // Handle back button for sub-destinations
    BackHandler(enabled = currentRoute != "home") {
        when (currentRoute) {
            "pressure_detector" -> viewModel.setRoute("pay_guard")
            "scam_gym" -> viewModel.setRoute("home")
            else -> viewModel.setRoute("home")
        }
    }

    val screenSubtitle = when (currentRoute) {
        "home" -> "Home Dashboard"
        "pay_guard" -> "Pay Guard"
        "alerts" -> "Safety Alerts"
        "voice" -> "Voice Assistant"
        "recovery" -> "Safety Recovery"
        "scam_gym" -> "Scam Gym Practice"
        "pressure_detector" -> "Pressure Detector"
        else -> "Safety Companion"
    }

    Scaffold(
        topBar = {
            if (currentRoute != "pressure_detector") {
                PaySaathiTopBar(
                    subtitle = screenSubtitle,
                    currentLanguage = currentLanguage,
                    onLanguageClick = {
                        val nextLang = when (currentLanguage) {
                            AppLanguage.ENGLISH -> AppLanguage.HINDI
                            AppLanguage.HINDI -> AppLanguage.KANNADA
                            AppLanguage.KANNADA -> AppLanguage.ENGLISH
                        }
                        viewModel.setLanguage(nextLang)
                    },
                    textSizeScale = textSizeScale,
                    onTextSizeClick = {
                        val nextScale = when (textSizeScale) {
                            "regular" -> "large"
                            "large" -> "xlarge"
                            else -> "regular"
                        }
                        viewModel.setTextSizeScale(nextScale)
                    },
                    onProfileClick = {
                        showFamilyDialog = true
                    }
                )
            }
        },
        bottomBar = {
            PaySaathiBottomNav(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    viewModel.setRoute(route)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                "home" -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToPayGuard = { viewModel.setRoute("pay_guard") },
                    onNavigateToAlerts = { viewModel.setRoute("alerts") },
                    onNavigateToScamGym = { viewModel.setRoute("scam_gym") },
                    onNavigateToRecovery = { viewModel.setRoute("recovery") },
                    onNavigateToVoice = { viewModel.setRoute("voice") }
                )
                "pay_guard" -> PayGuardScreen(
                    viewModel = viewModel,
                    onOpenPressureDetector = { viewModel.setRoute("pressure_detector") },
                    onCallFamily = { showFamilyDialog = true },
                    onAskSaathi = { viewModel.setRoute("voice") }
                )
                "alerts" -> MessageExplainerScreen(
                    viewModel = viewModel,
                    onAskSaathi = { viewModel.setRoute("voice") }
                )
                "scam_gym" -> ScamGymScreen(
                    viewModel = viewModel
                )
                "recovery" -> SafetyRecoveryScreen(
                    viewModel = viewModel
                )
                "voice" -> VoiceAssistantScreen(
                    viewModel = viewModel,
                    onCallFamily = { showFamilyDialog = true }
                )
                "pressure_detector" -> PressureDetectorScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.setRoute("pay_guard") }
                )
            }

            // Floating Ask Saathi button on main screens except voice assistant
            if (currentRoute != "voice" && currentRoute != "pressure_detector") {
                FloatingAskSaathiButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 20.dp, end = 16.dp),
                    onClick = {
                        viewModel.setRoute("voice")
                    }
                )
            }

            // Call Family Modal
            if (showFamilyDialog) {
                CallFamilyDialog(
                    onCallFamily = { phone ->
                        viewModel.dialHelpline(context, phone)
                        showFamilyDialog = false
                    },
                    onCallHelpline = {
                        viewModel.dialHelpline(context, "1930")
                        showFamilyDialog = false
                    },
                    onDismiss = {
                        showFamilyDialog = false
                    }
                )
            }
        }
    }
}
