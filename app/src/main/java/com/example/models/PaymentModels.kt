package com.example.models

enum class PaymentType(val title: String) {
    DIRECT_DEBIT("Direct Pay"),
    COLLECT_REQUEST("Collect Request"),
    QR_SCAN("Scanned QR Code")
}

enum class RiskLevel {
    LOW_RISK,
    HOLD_UP,
    RED_ALERT
}

enum class InspectionStep(val stepNumber: Int, val title: String) {
    PAYEE(1, "Payee Details"),
    INTENT(2, "Intent"),
    CHECKLIST(3, "Safety Check"),
    VERDICT(4, "Verdict")
}

data class ChecklistAnswer(
    val questionId: Int,
    val questionText: String,
    val answer: String // "YES", "NO", "NOT_SURE"
)

data class PaymentVerificationResult(
    val payeeName: String,
    val upiId: String,
    val amount: Double,
    val paymentType: PaymentType,
    val userIntent: String,
    val riskLevel: RiskLevel,
    val riskHeadline: String,
    val riskReasoning: String,
    val psychologicalTactics: List<PsychologicalTactic>,
    val stressIndexPercent: Int,
    val speechScriptEnglish: String,
    val speechScriptHindi: String,
    val speechScriptKannada: String,
    val canProceedSafely: Boolean
)

data class PsychologicalTactic(
    val id: String,
    val title: String,
    val subtitle: String,
    val isDetected: Boolean,
    val tacticDescription: String,
    val safetyGuidance: String
)
