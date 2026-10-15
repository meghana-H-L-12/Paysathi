package com.example.models

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val symbol: String,
    val greeting: String,
    val tagline: String,
    val voicePrompt: String
) {
    ENGLISH(
        code = "en",
        nativeName = "English",
        englishName = "English",
        symbol = "En",
        greeting = "Namaste, Ramesh Ji!",
        tagline = "Stay Safe. Pay Confidently.",
        voicePrompt = "Ask in your own words..."
    ),
    HINDI(
        code = "hi",
        nativeName = "हिन्दी",
        englishName = "Hindi",
        symbol = "अ",
        greeting = "नमस्ते, रमेश जी!",
        tagline = "सुरक्षित रहें। विश्वास से भुगतान करें।",
        voicePrompt = "बोलकर पूछें, हम सुन रहे हैं..."
    ),
    KANNADA(
        code = "kn",
        nativeName = "ಕನ್ನಡ",
        englishName = "Kannada",
        symbol = "ಕ",
        greeting = "ನಮಸ್ಕಾರ, ರಮೇಶ್ ಅವರೇ!",
        tagline = "ಸುರಕ್ಷಿತವಾಗಿರಿ. ವಿಶ್ವಾಸದಿಂದ ಪಾವತಿಸಿ.",
        voicePrompt = "ನಿಮ್ಮ ಸ್ವಂತ ಭಾಷೆಯಲ್ಲಿ ಮಾತನಾಡಿ..."
    );

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
