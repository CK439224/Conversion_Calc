package com.numconversion.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun completeOnePlusTwo() {
        composeTestRule.onNodeWithTag("1").performClick()
        composeTestRule.onNodeWithTag("+").performClick()
        composeTestRule.onNodeWithTag("2").performClick()
        composeTestRule.onNodeWithTag("=").performClick()
    }

    @Test
    fun completingACalculationAddsAHistoryEntry() {
        completeOnePlusTwo()
        composeTestRule.onNodeWithText("History").performClick()
        composeTestRule.onNodeWithText("1+2 = 3").assertIsDisplayed()
    }

    @Test
    fun clearEmptiesHistory() {
        completeOnePlusTwo()
        composeTestRule.onNodeWithText("History").performClick()
        composeTestRule.onNodeWithText("Clear").performClick()
        composeTestRule.onNodeWithText("No history yet").assertIsDisplayed()
    }
}
