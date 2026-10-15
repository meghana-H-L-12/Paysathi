package com.example.models

enum class MessageCategory(val label: String) {
    SUSPICIOUS_PHISHING("Suspicious Phishing Scam"),
    BANK_DEBIT_CREDIT("Bank Debit / Credit"),
    OTP_VERIFICATION("OTP Code"),
    PROMOTIONAL("Promotional Alert")
}

data class WarningSign(
    val title: String,
    val description: String,
    val isRedFlag: Boolean = true
)

data class MessageAnalysis(
    val originalText: String,
    val category: MessageCategory,
    val riskLevel: RiskLevel,
    val verdictTitle: String,
    val verdictSubtitle: String,
    val plainMeaningHindi: String,
    val plainMeaningEnglish: String,
    val plainMeaningKannada: String,
    val warningSigns: List<WarningSign>,
    val actionChecklist: List<String>
)
