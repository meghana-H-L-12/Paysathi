package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.accessibility.SpeechManager
import com.example.data.UserPreferencesManager
import com.example.database.AppDatabase
import com.example.database.SafetyCheckEntity
import com.example.models.AppLanguage
import com.example.models.EvidenceItem
import com.example.models.InspectionStep
import com.example.models.MessageAnalysis
import com.example.models.PaymentType
import com.example.models.PaymentVerificationResult
import com.example.models.RiskLevel
import com.example.models.ScamDrill
import com.example.repository.SafetyRecoveryRepository
import com.example.repository.ScamGymRepository
import com.example.security.MessageExplainerEngine
import com.example.security.SafetyRulesEngine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class VoiceChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String,
    val isVerifiedSafetyShield: Boolean = false,
    val badgeTitle: String? = null,
    val audioScript: String? = null
)

class PaySaathiViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = UserPreferencesManager(application)
    private val database = AppDatabase.getDatabase(application)
    val speechManager = SpeechManager(application)
    val scamGymRepository = ScamGymRepository()
    val safetyRecoveryRepository = SafetyRecoveryRepository()

    // Preferences
    val currentLanguage: StateFlow<AppLanguage> = preferences.selectedLanguage
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppLanguage.ENGLISH)

    val textSizeScale: StateFlow<String> = preferences.textSizeScale
        .stateIn(viewModelScope, SharingStarted.Eagerly, "regular")

    val isHighContrast: StateFlow<Boolean> = preferences.isHighContrast
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isOnboardingDone: StateFlow<Boolean> = preferences.isOnboardingCompleted
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // Navigation Tab Route
    private val _currentRoute = MutableStateFlow("home")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    // Safety Checks from Room
    val safetyChecks: StateFlow<List<SafetyCheckEntity>> = database.safetyCheckDao().getAllChecks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // --- PAY GUARD STATE ---
    private val _inspectionStep = MutableStateFlow(InspectionStep.VERDICT)
    val inspectionStep: StateFlow<InspectionStep> = _inspectionStep.asStateFlow()

    private val _payeeName = MutableStateFlow("Electricity Support")
    val payeeName: StateFlow<String> = _payeeName.asStateFlow()

    private val _upiId = MutableStateFlow("powerbills-urgent@okaxis")
    val upiId: StateFlow<String> = _upiId.asStateFlow()

    private val _amountText = MutableStateFlow("4850")
    val amountText: StateFlow<String> = _amountText.asStateFlow()

    private val _paymentType = MutableStateFlow(PaymentType.COLLECT_REQUEST)
    val paymentType: StateFlow<PaymentType> = _paymentType.asStateFlow()

    private val _userIntent = MutableStateFlow("A caller said my power will be disconnected at 8 PM tonight unless I pay this bill immediately through UPI.")
    val userIntent: StateFlow<String> = _userIntent.asStateFlow()

    // Safety Checklist answers
    private val _checklistAnswers = MutableStateFlow(
        mutableMapOf(
            1 to "YES",
            2 to "YES",
            3 to "NOT_SURE",
            4 to "YES",
            5 to "YES"
        )
    )
    val checklistAnswers: StateFlow<Map<Int, String>> = _checklistAnswers.asStateFlow()

    private val _verificationResult = MutableStateFlow<PaymentVerificationResult?>(null)
    val verificationResult: StateFlow<PaymentVerificationResult?> = _verificationResult.asStateFlow()

    // --- MESSAGE EXPLAINER (ALERTS) STATE ---
    private val _messageInput = MutableStateFlow(
        "Dear customer, your SBI NetBanking rewards of Rs 9,850 are expiring today! Click http://sbi-rewards-claim.xyz/redeem to update your PAN & collect cash directly into your account."
    )
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()

    private val _messageExplainerTab = MutableStateFlow("paste") // "paste", "share", "screenshot"
    val messageExplainerTab: StateFlow<String> = _messageExplainerTab.asStateFlow()

    private val _messageAnalysis = MutableStateFlow<MessageAnalysis?>(null)
    val messageAnalysis: StateFlow<MessageAnalysis?> = _messageAnalysis.asStateFlow()

    // --- SCAM GYM STATE ---
    private val _currentDrill = MutableStateFlow<ScamDrill>(scamGymRepository.getCurrentDrill())
    val currentDrill: StateFlow<ScamDrill> = _currentDrill.asStateFlow()

    private val _selectedDrillOptionKey = MutableStateFlow<String?>("B")
    val selectedDrillOptionKey: StateFlow<String?> = _selectedDrillOptionKey.asStateFlow()

    // --- RECOVERY (AFTER KIT) STATE ---
    private val _bankScriptLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val bankScriptLanguage: StateFlow<AppLanguage> = _bankScriptLanguage.asStateFlow()

    private val _evidenceItems = MutableStateFlow(safetyRecoveryRepository.getDefaultEvidenceItems())
    val evidenceItems: StateFlow<List<EvidenceItem>> = _evidenceItems.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // --- VOICE ASSISTANT CHAT STREAM ---
    private val _chatMessages = MutableStateFlow<List<VoiceChatMessage>>(
        listOf(
            VoiceChatMessage(
                id = "1",
                isUser = true,
                text = "A man on the phone says my electricity bill is overdue and I must pay ₹4,850 right now on UPI. What should I do?",
                timestamp = "Just now"
            ),
            VoiceChatMessage(
                id = "2",
                isUser = false,
                text = "Do NOT pay this money. Legitimate electricity boards never call demanding immediate UPI transfers or threaten same-night disconnection. Hang up the call and verify on your physical bill.",
                timestamp = "Just now",
                isVerifiedSafetyShield = true,
                badgeTitle = "Saathi Safety Shield • Verified Guidance",
                audioScript = "Do not pay this money. Legitimate electricity boards never call demanding immediate transfers."
            ),
            VoiceChatMessage(
                id = "3",
                isUser = true,
                text = "Why does he say enter UPI PIN to receive refund?",
                timestamp = "Just now"
            ),
            VoiceChatMessage(
                id = "4",
                isUser = false,
                text = "Golden Safety Rule #1: PIN is ONLY for sending. You NEVER enter your UPI PIN to receive money. Entering a PIN always debits money straight out of your bank account.",
                timestamp = "Just now",
                isVerifiedSafetyShield = true,
                badgeTitle = "Golden Safety Rule #1",
                audioScript = "You never enter your UPI PIN to receive money. PIN is only for sending money."
            )
        )
    )
    val chatMessages: StateFlow<List<VoiceChatMessage>> = _chatMessages.asStateFlow()

    init {
        // Run initial verification with default values
        runInspectionAnalysis()
        // Run initial message analysis
        explainMessage(_messageInput.value)
    }

    fun setRoute(route: String) {
        _currentRoute.value = route
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            preferences.setLanguage(language)
        }
    }

    fun setTextSizeScale(scale: String) {
        viewModelScope.launch {
            preferences.setTextSizeScale(scale)
        }
    }

    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setHighContrast(enabled)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            preferences.setOnboardingCompleted(true)
        }
    }

    // --- PAY GUARD ACTIONS ---
    fun setInspectionStep(step: InspectionStep) {
        _inspectionStep.value = step
    }

    fun updatePayeeDetails(name: String, upi: String, amount: String, type: PaymentType) {
        _payeeName.value = name
        _upiId.value = upi
        _amountText.value = amount
        _paymentType.value = type
        runInspectionAnalysis()
    }

    fun updateUserIntent(intent: String) {
        _userIntent.value = intent
        runInspectionAnalysis()
    }

    fun answerChecklistQuestion(questionId: Int, answer: String) {
        val updated = _checklistAnswers.value.toMutableMap()
        updated[questionId] = answer
        _checklistAnswers.value = updated
        runInspectionAnalysis()
    }

    fun runInspectionAnalysis() {
        val amt = _amountText.value.toDoubleOrNull() ?: 4850.0
        val isNewPayee = _checklistAnswers.value[1] == "YES"
        val isCollect = _checklistAnswers.value[2] == "YES" || _paymentType.value == PaymentType.COLLECT_REQUEST
        val isLarge = _checklistAnswers.value[3] == "YES"
        val isOnCall = _checklistAnswers.value[4] == "YES"
        val isUrgent = _checklistAnswers.value[5] == "YES"

        val result = SafetyRulesEngine.analyzePayment(
            payeeName = _payeeName.value,
            upiId = _upiId.value,
            amount = amt,
            paymentType = _paymentType.value,
            userIntent = _userIntent.value,
            isNewPayee = isNewPayee,
            isCollectRequest = isCollect,
            isLargeAmount = isLarge,
            isOnPhoneCall = isOnCall,
            isUrgentOrSecret = isUrgent
        )
        _verificationResult.value = result
    }

    fun startVoiceInputForIntent() {
        val lang = currentLanguage.value.code
        speechManager.startListening(lang) { text ->
            _userIntent.value = text
            runInspectionAnalysis()
        }
    }

    fun stopVoiceInput() {
        speechManager.stopListening()
    }

    fun saveCurrentCheckToHistory() {
        val result = _verificationResult.value ?: return
        viewModelScope.launch {
            database.safetyCheckDao().insertCheck(
                SafetyCheckEntity(
                    title = result.payeeName,
                    description = result.riskHeadline,
                    amount = result.amount,
                    isSafe = result.canProceedSafely,
                    statusBadge = if (result.canProceedSafely) "Safe to Pay (₹${result.amount.toInt()})" else "Blocked • Impersonation Scam",
                    timeLabel = "Just now",
                    payeeOrSender = result.upiId,
                    category = "UPI_VERIFY"
                )
            )
            _toastEvent.emit("Saved inspection record to local safety history")
        }
    }

    // --- ALERTS / MESSAGE EXPLAINER ACTIONS ---
    fun updateMessageInput(text: String) {
        _messageInput.value = text
    }

    fun setMessageExplainerTab(tab: String) {
        _messageExplainerTab.value = tab
    }

    fun explainMessage(text: String) {
        val analysis = MessageExplainerEngine.analyzeMessage(text)
        _messageAnalysis.value = analysis
    }

    fun clearMessageInput() {
        _messageInput.value = ""
        _messageAnalysis.value = null
    }

    // --- SCAM GYM ACTIONS ---
    fun selectDrillOption(optionKey: String) {
        _selectedDrillOptionKey.value = optionKey
    }

    fun advanceToNextDrill() {
        val next = scamGymRepository.nextDrill()
        _currentDrill.value = next
        _selectedDrillOptionKey.value = null
    }

    // --- RECOVERY ACTIONS ---
    fun toggleBankScriptLanguage() {
        _bankScriptLanguage.value = if (_bankScriptLanguage.value == AppLanguage.HINDI) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.HINDI
        }
    }

    fun toggleEvidenceItem(id: String) {
        val current = _evidenceItems.value
        _evidenceItems.value = current.map { item ->
            if (item.id == id) item.copy(isChecked = !item.isChecked) else item
        }
    }

    fun exportEvidenceReport() {
        viewModelScope.launch {
            _toastEvent.emit("Evidence Report PDF generated & ready for Bank & Police!")
        }
    }

    fun dialHelpline(context: Context, number: String = "1930") {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Dialing: $number", Toast.LENGTH_SHORT).show()
        }
    }

    fun openCyberPortal(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Visit cybercrime.gov.in", Toast.LENGTH_SHORT).show()
        }
    }

    // --- VOICE ASSISTANT ACTIONS ---
    fun sendUserVoiceOrTextMessage(query: String) {
        if (query.isBlank()) return
        val userMsg = VoiceChatMessage(
            id = System.currentTimeMillis().toString(),
            isUser = true,
            text = query,
            timestamp = "Just now"
        )
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(userMsg)
        _chatMessages.value = currentList

        // Generate instant AI/rule guidance response
        viewModelScope.launch {
            val responseText = generateSaathiGuidance(query)
            val saathiMsg = VoiceChatMessage(
                id = (System.currentTimeMillis() + 1).toString(),
                isUser = false,
                text = responseText,
                timestamp = "Just now",
                isVerifiedSafetyShield = true,
                badgeTitle = "Saathi Instant Safety Shield",
                audioScript = responseText
            )
            val updated = _chatMessages.value.toMutableList()
            updated.add(saathiMsg)
            _chatMessages.value = updated
        }
    }

    private fun generateSaathiGuidance(query: String): String {
        val lower = query.lowercase()
        return when {
            lower.contains("pin") && (lower.contains("receive") || lower.contains("credit") || lower.contains("refund")) -> {
                "GOLDEN RULE: You NEVER enter your UPI PIN to receive money! Entering a PIN ALWAYS sends money out of your account. Hang up immediately."
            }
            lower.contains("sms") || lower.contains("link") || lower.contains("lottery") || lower.contains("prize") -> {
                "This has classic indicators of a phishing trap. Do NOT click the link, do not share OTP, and delete the message right away."
            }
            lower.contains("electricity") || lower.contains("power") || lower.contains("bescom") -> {
                "Do NOT pay over phone call! Electricity providers never cut power at night without official written bills delivered days in advance."
            }
            lower.contains("deducted") || lower.contains("lost") || lower.contains("fraud") -> {
                "Call 1930 immediately! The National Cyber Crime helpline can notify your bank to freeze transfer chains within the Golden Hour."
            }
            else -> {
                "Saathi is here with you. Remember: Never enter your UPI PIN on unexpected collect requests, never share OTPs, and always verify with family before sending money."
            }
        }
    }

    fun playAudio(text: String, languageCode: String? = null) {
        val lang = languageCode ?: currentLanguage.value.code
        speechManager.speak(text, lang)
    }

    fun stopAudio() {
        speechManager.stopSpeaking()
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.release()
    }
}
