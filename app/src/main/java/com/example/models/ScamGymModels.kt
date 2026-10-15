package com.example.models

data class ScamDrillOption(
    val optionKey: String, // "A", "B", "C"
    val mainText: String,
    val subText: String,
    val isCorrect: Boolean,
    val feedbackTitle: String,
    val feedbackExplanation: String,
    val isRecommended: Boolean = false
)

data class ScamDrill(
    val id: String,
    val scenarioIndex: Int,
    val totalScenarios: Int,
    val title: String,
    val contextStory: String,
    val requesterName: String,
    val amountRequested: Double,
    val alertType: String,
    val requirementNote: String,
    val hintText: String,
    val options: List<ScamDrillOption>,
    val goldenRuleTitle: String,
    val goldenRuleQuote: String
)

data class DrillStats(
    val completedCount: Int,
    val totalAvailable: Int,
    val scorePercentage: Int,
    val levelName: String,
    val badgeName: String,
    val growthPercent: Int
)
