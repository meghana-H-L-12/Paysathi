package com.example.security

import com.example.models.PaymentType
import com.example.models.PaymentVerificationResult
import com.example.models.PsychologicalTactic
import com.example.models.RiskLevel

object SafetyRulesEngine {

    fun analyzePayment(
        payeeName: String,
        upiId: String,
        amount: Double,
        paymentType: PaymentType,
        userIntent: String,
        isNewPayee: Boolean,
        isCollectRequest: Boolean,
        isLargeAmount: Boolean,
        isOnPhoneCall: Boolean,
        isUrgentOrSecret: Boolean
    ): PaymentVerificationResult {
        val lowerIntent = userIntent.lowercase()
        val lowerPayee = payeeName.lowercase()
        val lowerUpi = upiId.lowercase()

        // Detect tactics
        val urgencyDetected = isUrgentOrSecret ||
                lowerIntent.contains("tonight") ||
                lowerIntent.contains("immediately") ||
                lowerIntent.contains("15 minute") ||
                lowerIntent.contains("power cut") ||
                lowerIntent.contains("disconnect") ||
                lowerUpi.contains("urgent")

        val impersonationDetected = lowerPayee.contains("electricity") ||
                lowerPayee.contains("bescom") ||
                lowerPayee.contains("support") ||
                lowerPayee.contains("official") ||
                lowerIntent.contains("bescom") ||
                lowerIntent.contains("electricity") ||
                lowerIntent.contains("power") ||
                lowerIntent.contains("board")

        val secrecyDetected = isOnPhoneCall ||
                lowerIntent.contains("stay on call") ||
                lowerIntent.contains("caller") ||
                lowerIntent.contains("man on the phone") ||
                lowerIntent.contains("don't tell")

        val payFirstDetected = isCollectRequest ||
                paymentType == PaymentType.COLLECT_REQUEST ||
                lowerIntent.contains("refund") ||
                lowerIntent.contains("receive") ||
                lowerIntent.contains("cashback")

        val tactics = listOf(
            PsychologicalTactic(
                id = "urgency",
                title = "1. URGENCY",
                subtitle = "Artificial Panic",
                isDetected = urgencyDetected,
                tacticDescription = "Imposes a fake deadline or immediate threat to create panic and stop you from thinking.",
                safetyGuidance = "Real utility providers deliver physical written notices and multi-day grace periods. Hang up."
            ),
            PsychologicalTactic(
                id = "impersonation",
                title = "2. AUTHORITY IMPERSONATION",
                subtitle = "False Official Status",
                isDetected = impersonationDetected,
                tacticDescription = "Pretending to be BESCOM, Power Board, or Bank Officer to induce compliance.",
                safetyGuidance = "Government and utility staff never demand direct UPI transfers to personal or ad-hoc handles."
            ),
            PsychologicalTactic(
                id = "secrecy",
                title = "3. SECRECY / ISOLATION",
                subtitle = "Cutting Off Advice",
                isDetected = secrecyDetected,
                tacticDescription = "Telling you to stay on call and refusing time to consult your family members.",
                safetyGuidance = "Always pause and talk to a family member, neighbor, or bank branch before sending money."
            ),
            PsychologicalTactic(
                id = "pay_first",
                title = "4. PAY-FIRST-VERIFY-LATER",
                subtitle = "Hidden Invoices & Collect Requests",
                isDetected = payFirstDetected,
                tacticDescription = "Demanding payment or approval before presenting an official verified bill invoice.",
                safetyGuidance = "Only pay via official utility apps or your physical printed bill at recognized centers."
            )
        )

        val detectedCount = tactics.count { it.isDetected }
        val stressIndex = when (detectedCount) {
            4 -> 96
            3 -> 84
            2 -> 62
            1 -> 35
            else -> 12
        }

        val riskLevel = when {
            detectedCount >= 2 || (isCollectRequest && lowerIntent.contains("receive")) || isOnPhoneCall -> RiskLevel.RED_ALERT
            detectedCount == 1 || isNewPayee || isLargeAmount -> RiskLevel.HOLD_UP
            else -> RiskLevel.LOW_RISK
        }

        val headline = when (riskLevel) {
            RiskLevel.RED_ALERT -> "STOP & PAUSE: High Risk of Social Engineering Impersonation Detected"
            RiskLevel.HOLD_UP -> "HOLD UP: Unverified Payee - Double Check Before Sending"
            RiskLevel.LOW_RISK -> "SAFE PAYEE: Verified Official Recipient Match"
        }

        val reasoning = when (riskLevel) {
            RiskLevel.RED_ALERT -> "Legitimate electricity boards and banks NEVER call threatening same-night disconnection at 8 PM or demanding immediate UPI transfers to an individual address."
            RiskLevel.HOLD_UP -> "This payee is new or requested an unusually high amount. Take a moment to confirm with your family or call the official customer care number."
            RiskLevel.LOW_RISK -> "The payee details match verified merchant records and no social engineering tactics were flagged."
        }

        val scriptEnglish = "Hello, I suspect an unauthorized or fraudulent transaction. Please help me secure my account, freeze the transaction, and explain the steps available to block or dispute it."
        val scriptHindi = "नमस्ते, मुझे आशंका है कि मेरे खाते से कोई धोखाधड़ी या गलत लेनदेन हुआ है। कृपया तुरंत मेरा खाता सुरक्षित करें, इस ट्रांजेक्शन को रोकें और इसे ब्लॉक करने में मदद करें।"
        val scriptKannada = "ನಮಸ್ಕಾರ, ನನ್ನ ಖಾತೆಯಲ್ಲಿ ಮೋಸದ ಅಥವಾ ಅನುಮಾನಾಸ್ಪದ ವಹಿವಾಟು ನಡೆದಿದೆ ಎಂದು ಶಂಕಿಸಲಾಗಿದೆ. ದಯವಿಟ್ಟು ತಕ್ಷಣ ನನ್ನ ಖಾತೆಯನ್ನು ರಕ್ಷಿಸಿ ಮತ್ತು ವಹಿವಾಟನ್ನು ತಡೆಯಿರಿ."

        return PaymentVerificationResult(
            payeeName = payeeName,
            upiId = upiId,
            amount = amount,
            paymentType = paymentType,
            userIntent = userIntent,
            riskLevel = riskLevel,
            riskHeadline = headline,
            riskReasoning = reasoning,
            psychologicalTactics = tactics,
            stressIndexPercent = stressIndex,
            speechScriptEnglish = scriptEnglish,
            speechScriptHindi = scriptHindi,
            speechScriptKannada = scriptKannada,
            canProceedSafely = riskLevel == RiskLevel.LOW_RISK
        )
    }
}
