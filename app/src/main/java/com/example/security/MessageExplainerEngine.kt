package com.example.security

import com.example.models.MessageAnalysis
import com.example.models.MessageCategory
import com.example.models.RiskLevel
import com.example.models.WarningSign

object MessageExplainerEngine {

    fun analyzeMessage(text: String): MessageAnalysis {
        val lower = text.lowercase()

        val isPhishing = lower.contains("http") || lower.contains(".xyz") || lower.contains(".cc") ||
                lower.contains("rewards") || lower.contains("claim") || lower.contains("lottery") ||
                lower.contains("pan") || lower.contains("kyc") || lower.contains("expir") ||
                lower.contains("prize")

        val isOtp = lower.contains("otp") || lower.contains("secret code") || lower.contains("verification code")
        val isDebit = lower.contains("debited") || lower.contains("credited") || lower.contains("withdrawn")

        val category = when {
            isPhishing -> MessageCategory.SUSPICIOUS_PHISHING
            isOtp -> MessageCategory.OTP_VERIFICATION
            isDebit -> MessageCategory.BANK_DEBIT_CREDIT
            else -> MessageCategory.PROMOTIONAL
        }

        val riskLevel = when {
            isPhishing -> RiskLevel.RED_ALERT
            isOtp && (lower.contains("share") || lower.contains("give") || lower.contains("send")) -> RiskLevel.RED_ALERT
            isOtp -> RiskLevel.HOLD_UP
            else -> RiskLevel.LOW_RISK
        }

        val warningSigns = mutableListOf<WarningSign>()

        if (lower.contains("http") || lower.contains(".xyz") || lower.contains(".top") || lower.contains("bit.ly")) {
            warningSigns.add(
                WarningSign(
                    title = "Fake Unofficial Website Link",
                    description = "Uses suspicious web address instead of official bank domain (.co.in or .bank.in). Anyone can register fake domains."
                )
            )
        }

        if (lower.contains("expir") || lower.contains("today") || lower.contains("immediately") || lower.contains("urgent") || lower.contains("now")) {
            warningSigns.add(
                WarningSign(
                    title = "Fake Urgency (\"Expiring Today!\")",
                    description = "Scammers rush you to act without thinking. Real bank reward points never expire in panic 24-hour windows."
                )
            )
        }

        if (lower.contains("reward") || lower.contains("cash") || lower.contains("prize") || lower.contains("lottery") || lower.contains("free")) {
            warningSigns.add(
                WarningSign(
                    title = "Free Cash Bait",
                    description = "Greed hooks are common deceptive hooks. Legitimate rewards do not require clicking odd links to collect money."
                )
            )
        }

        if (lower.contains("pan") || lower.contains("kyc") || lower.contains("password") || lower.contains("otp") || lower.contains("pin")) {
            warningSigns.add(
                WarningSign(
                    title = "Request for PAN, OTP or Bank Credentials",
                    description = "A standard pretext to bypass two-factor authentications or clone identity for loan and bank fraud."
                )
            )
        }

        if (warningSigns.isEmpty()) {
            warningSigns.add(
                WarningSign(
                    title = "General Transaction Notification",
                    description = "Standard alert without suspicious links or sensitive credential requests.",
                    isRedFlag = false
                )
            )
        }

        val verdictTitle = if (riskLevel == RiskLevel.RED_ALERT) "SUSPICIOUS — Phishing Trap" else "Verified Standard Message"
        val verdictSubtitle = if (riskLevel == RiskLevel.RED_ALERT) {
            "Do not click any link or send money. This is an attempt to steal your data."
        } else {
            "No harmful links or credential harvesting patterns identified."
        }

        val meaningEnglish = if (riskLevel == RiskLevel.RED_ALERT) {
            "“This message is pretending to be your bank or provider offering free reward cash or threatening closure. In reality, it is a cyber-trap trying to steal your bank password, PIN, and identity.”"
        } else {
            "“This looks like a regular notification from your service provider. Make sure to keep your OTP and UPI PIN secret regardless.”"
        }

        val meaningHindi = if (riskLevel == RiskLevel.RED_ALERT) {
            "“यह संदेश बैंक का मुखौटा पहनकर आपको लालच या डर दिखा रहा है। वास्तव में, यह आपके बैंक पासवर्ड, ओटीपी और पैसे चुराने का साइबर जाल है।”"
        } else {
            "“यह एक सामान्य सूचना संदेश प्रतीत होता है। फिर भी किसी से भी अपना पिन या पासवर्ड साझा न करें।”"
        }

        val meaningKannada = if (riskLevel == RiskLevel.RED_ALERT) {
            "“ಈ ಸಂದೇಶವು ನಿಮ್ಮ ಬ್ಯಾಂಕಿನಂತೆ ನಟಿಸುತ್ತಿದೆ. ವಾಸ್ತವವಾಗಿ ಇದು ನಿಮ್ಮ ಖಾತೆಯ ರಹಸ್ಯ ಪಿನ್ ಮತ್ತು ಹಣವನ್ನು ಕದಿಯುವ ಸೈಬರ್ ಬಲೆಯಾಗಿದೆ.”"
        } else {
            "“ಇದು ಸಾಮಾನ್ಯ ಬ್ಯಾಂಕ್ ಸಂದೇಶದಂತೆ ಕಾಣುತ್ತದೆ. ಆದರೂ ನಿಮ್ಮ ರಹಸ್ಯ ಪಿನ್ ಯಾರೊಂದಿಗೂ ಹಂಚಿಕೊಳ್ಳಬೇಡಿ.”"
        }

        val actions = listOf(
            "Do NOT click the web link or open it in a browser.",
            "Never share OTP, PIN, or PAN card details.",
            "Delete the SMS and block the sending phone number."
        )

        return MessageAnalysis(
            originalText = text,
            category = category,
            riskLevel = riskLevel,
            verdictTitle = verdictTitle,
            verdictSubtitle = verdictSubtitle,
            plainMeaningHindi = meaningHindi,
            plainMeaningEnglish = meaningEnglish,
            plainMeaningKannada = meaningKannada,
            warningSigns = warningSigns,
            actionChecklist = actions
        )
    }
}
