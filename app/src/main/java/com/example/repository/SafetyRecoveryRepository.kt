package com.example.repository

import com.example.models.EmergencyBankContact
import com.example.models.EvidenceItem

class SafetyRecoveryRepository {

    val emergencyBanks = listOf(
        EmergencyBankContact("SBI Bank", "18001234", "1800 1234"),
        EmergencyBankContact("HDFC Bank", "18002583838", "1800 258 3838"),
        EmergencyBankContact("ICICI Bank", "18001080", "1800 1080"),
        EmergencyBankContact("Axis Bank", "18002095555", "1800 209 5555"),
        EmergencyBankContact("Punjab National", "18001802222", "1800 180 2222"),
        EmergencyBankContact("Bank of Baroda", "18002584455", "1800 258 4455")
    )

    fun getDefaultEvidenceItems(): List<EvidenceItem> {
        return listOf(
            EvidenceItem(
                id = "utr",
                title = "Transaction ID / 12-Digit UTR Number",
                subtitle = "Found in your SMS receipt or UPI history",
                isChecked = true
            ),
            EvidenceItem(
                id = "datetime",
                title = "Date & Exact Time of Transfer",
                subtitle = "Matches bank server log time",
                isChecked = true
            ),
            EvidenceItem(
                id = "amount",
                title = "Amount Transferred (₹)",
                subtitle = "Accurate debit value including fees",
                isChecked = true
            ),
            EvidenceItem(
                id = "scammer_id",
                title = "Scammer's UPI ID or Phone Number",
                subtitle = "Destination VPA handle or phone digits",
                isChecked = false
            ),
            EvidenceItem(
                id = "screenshot",
                title = "Screenshot of Chat, WhatsApp or SMS",
                subtitle = "Supports investigation proof",
                isChecked = false
            )
        )
    }

    val scriptEnglish = "“Hello, I suspect an unauthorized or fraudulent transaction. Please help me secure my account, freeze the transaction, and explain the steps available to block or dispute it.”"

    val scriptHindi = "“नमस्ते, मुझे आशंका है कि मेरे खाते से कोई धोखाधड़ी या गलत लेनदेन हुआ है। कृपया तुरंत मेरा खाता सुरक्षित करें, इस ट्रांजेक्शन को रोकें और इसे ब्लॉक करने में मदद करें।”"

    val scriptKannada = "“ನಮಸ್ಕಾರ, ನನ್ನ ಬ್ಯಾಂಕ್ ಖಾತೆಯಿಂದ ಅನಧಿಕೃತ ಅಥವಾ ವಂಚನೆಯ ವಹಿವಾಟು ನಡೆದಿರುವ ಶಂಕೆ ಇದೆ. ದಯವಿಟ್ಟು ತಕ್ಷಣ ನನ್ನ ಖಾತೆಯನ್ನು ರಕ್ಷಿಸಿ, ಈ ವಹಿವಾಟನ್ನು ಸ್ಥಗಿತಗೊಳಿಸಿ ಮತ್ತು ದೂರು ದಾಖಲಿಸಲು ಸಹಾಯ ಮಾಡಿ.”"
}
