package com.example.repository

import com.example.models.DrillStats
import com.example.models.ScamDrill
import com.example.models.ScamDrillOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScamGymRepository {

    private val drills = listOf(
        ScamDrill(
            id = "drill_pin_collect",
            scenarioIndex = 1,
            totalScenarios = 3,
            title = "The 'Receive Money by PIN' Trick",
            contextStory = "You posted an old sofa for sale online. A buyer messages: \"I am sending you ₹2,500 advance right now on PhonePe/GPay. Please approve the collect request and enter your UPI PIN to receive the money in your bank account.\"",
            requesterName = "Ramesh Kumar (Online Buyer)",
            amountRequested = 2500.0,
            alertType = "Incoming Collect",
            requirementNote = "Requirement: Enter UPI PIN to proceed",
            hintText = "Notice how this screen asks you to authorize a debit transfer.",
            options = listOf(
                ScamDrillOption(
                    optionKey = "A",
                    mainText = "Enter UPI PIN to accept the payment",
                    subText = "Assumes the money will credit immediately",
                    isCorrect = false,
                    feedbackTitle = "Careful! Money will be lost!",
                    feedbackExplanation = "If you punch in your UPI PIN here, ₹2,500 will be instantly debited from YOUR bank account. Never enter a PIN to receive payments."
                ),
                ScamDrillOption(
                    optionKey = "B",
                    mainText = "Reject! You NEVER need a PIN to RECEIVE money",
                    subText = "The golden safety rule for all banking users",
                    isCorrect = true,
                    feedbackTitle = "Spot on! Excellent catch!",
                    feedbackExplanation = "The Golden Rule of UPI: Entering your secure PIN always SENDS money out of your account, and NEVER brings money in! Scammers rely on rushed sellers to push approve.",
                    isRecommended = true
                ),
                ScamDrillOption(
                    optionKey = "C",
                    mainText = "Not sure — Pause and ask family first",
                    subText = "Safe second option to prevent emotional rush",
                    isCorrect = false,
                    feedbackTitle = "Wise Caution!",
                    feedbackExplanation = "Pausing and talking to a family member protects your savings. But remember: entering a PIN is only for sending money, never receiving!"
                )
            ),
            goldenRuleTitle = "Golden Safety Rule #1",
            goldenRuleQuote = "Entering your UPI PIN always sends money from your bank. Receiving money NEVER requires a PIN."
        ),
        ScamDrill(
            id = "drill_electricity_sms",
            scenarioIndex = 2,
            totalScenarios = 3,
            title = "Electricity Power Cut SMS Scare",
            contextStory = "You receive an SMS: \"Dear consumer, your electricity power will be disconnected at 8:30 PM tonight due to unpaid bill of ₹4,850. Call officer immediately at 9876543210.\"",
            requesterName = "BESCOM Billing Desk (Purported)",
            amountRequested = 4850.0,
            alertType = "Disconnection Notice",
            requirementNote = "Requirement: Immediate transfer via personal UPI",
            hintText = "Notice the arbitrary deadline and unknown personal mobile number.",
            options = listOf(
                ScamDrillOption(
                    optionKey = "A",
                    mainText = "Call the number and pay quickly to avoid dark home",
                    subText = "Panic reaction due to nighttime fear",
                    isCorrect = false,
                    feedbackTitle = "DANGER: High Risk Scam!",
                    feedbackExplanation = "Electricity boards never cut power without multiple physical written notices and official receipts. The caller is an imposter."
                ),
                ScamDrillOption(
                    optionKey = "B",
                    mainText = "Hang up, ignore caller, and verify only on official app or physical bill",
                    subText = "Calm verification via verified government utility portal",
                    isCorrect = true,
                    feedbackTitle = "Outstanding Defense!",
                    feedbackExplanation = "Utilities never disconnect power in 30 minutes over a phone call, and never ask for transfers to individual UPI handles.",
                    isRecommended = true
                ),
                ScamDrillOption(
                    optionKey = "C",
                    mainText = "Share the SMS with the scammer's WhatsApp",
                    subText = "Invites further social engineering pressure",
                    isCorrect = false,
                    feedbackTitle = "Avoid Communicating!",
                    feedbackExplanation = "Engaging with scammers over WhatsApp gives them more leverage. Block the sender and check your official bill."
                )
            ),
            goldenRuleTitle = "Golden Safety Rule #2",
            goldenRuleQuote = "Real electricity boards give written advance notices with official bill numbers. Never pay unexpected callers."
        ),
        ScamDrill(
            id = "drill_remote_app",
            scenarioIndex = 3,
            totalScenarios = 3,
            title = "The 'Install Screen Support App' Trap",
            contextStory = "A polite caller claims your bank KYC is pending. He asks you to install an app called 'QuickSupport' or an APK file sent on WhatsApp so he can 'help verify your Aadhaar'.",
            requesterName = "Customer Care Executive",
            amountRequested = 1.0,
            alertType = "Remote Screen Share",
            requirementNote = "Requirement: Install screen-mirroring app or unknown APK",
            hintText = "The app allows the caller to see your screen and read your secret OTPs.",
            options = listOf(
                ScamDrillOption(
                    optionKey = "A",
                    mainText = "Install the app because the executive is very polite",
                    subText = "Trusting tone without verifying identity",
                    isCorrect = false,
                    feedbackTitle = "CRITICAL TRAP!",
                    feedbackExplanation = "Screen-sharing apps like AnyDesk, QuickSupport, or TeamViewer allow thieves to watch you type your PIN and passwords, draining your bank in seconds."
                ),
                ScamDrillOption(
                    optionKey = "B",
                    mainText = "Refuse immediately! Never install screen-sharing apps for callers",
                    subText = "Zero trust policy for uninvited remote access",
                    isCorrect = true,
                    feedbackTitle = "Scam Master Move!",
                    feedbackExplanation = "Banks NEVER ask customers to install remote control or screen-mirroring software. You protected your account completely!",
                    isRecommended = true
                ),
                ScamDrillOption(
                    optionKey = "C",
                    mainText = "Send them a photo of your debit card instead",
                    subText = "Leaking sensitive card numbers and CVV",
                    isCorrect = false,
                    feedbackTitle = "Never Share Card Details!",
                    feedbackExplanation = "Never share debit card numbers, expiry dates, or CVV codes. Visit your local bank branch for official KYC."
                )
            ),
            goldenRuleTitle = "Golden Safety Rule #3",
            goldenRuleQuote = "Never install any app or click APK links sent by strangers. Banks never require screen sharing."
        )
    )

    private val _currentDrillIndex = MutableStateFlow(0)
    val currentDrillIndex: StateFlow<Int> = _currentDrillIndex.asStateFlow()

    private val _stats = MutableStateFlow(
        DrillStats(
            completedCount = 3,
            totalAvailable = 3,
            scorePercentage = 95,
            levelName = "Level 3",
            badgeName = "Scam Master Badge",
            growthPercent = 55
        )
    )
    val stats: StateFlow<DrillStats> = _stats.asStateFlow()

    fun getCurrentDrill(): ScamDrill {
        val index = _currentDrillIndex.value.coerceIn(0, drills.size - 1)
        return drills[index]
    }

    fun nextDrill(): ScamDrill {
        val next = (_currentDrillIndex.value + 1) % drills.size
        _currentDrillIndex.value = next
        return drills[next]
    }
}
