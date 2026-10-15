package com.example.models

data class EmergencyBankContact(
    val bankName: String,
    val phoneNumber: String,
    val formattedDisplay: String
)

data class EvidenceItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val isChecked: Boolean = false
)

data class DisputeReport(
    val transactionId: String,
    val transferDateTime: String,
    val amountTransferred: String,
    val scammerDetails: String,
    val bankName: String,
    val additionalNotes: String
)
