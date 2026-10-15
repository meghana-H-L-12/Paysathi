package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.models.PaymentType
import com.example.models.RiskLevel
import com.example.security.MessageExplainerEngine
import com.example.security.SafetyRulesEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name from string resources matches PaySaathi`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("PaySaathi", appName)
    }

    @Test
    fun `safety rules engine flags collect request scam as RED ALERT`() {
        val result = SafetyRulesEngine.analyzePayment(
            payeeName = "Electricity Support",
            upiId = "powerbills-urgent@okaxis",
            amount = 4850.0,
            paymentType = PaymentType.COLLECT_REQUEST,
            userIntent = "Caller says power will be cut in 15 minutes unless I pay",
            isNewPayee = true,
            isCollectRequest = true,
            isLargeAmount = true,
            isOnPhoneCall = true,
            isUrgentOrSecret = true
        )

        assertEquals(RiskLevel.RED_ALERT, result.riskLevel)
        assertTrue(result.stressIndexPercent >= 80)
        assertTrue(result.psychologicalTactics.any { it.id == "urgency" && it.isDetected })
        assertTrue(result.psychologicalTactics.any { it.id == "impersonation" && it.isDetected })
    }

    @Test
    fun `message explainer engine flags fake rewards domain phishing`() {
        val analysis = MessageExplainerEngine.analyzeMessage(
            "Dear customer, your SBI NetBanking rewards of Rs 9,850 are expiring today! Click http://sbi-rewards-claim.xyz/redeem to update your PAN & collect cash."
        )

        assertEquals(RiskLevel.RED_ALERT, analysis.riskLevel)
        assertNotNull(analysis.warningSigns.find { it.title.contains("Website Link") })
        assertTrue(analysis.actionChecklist.isNotEmpty())
    }
}
