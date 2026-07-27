package com.numconversion.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addingTwoNumbersShowsTheResult() {
        composeTestRule.onNodeWithTag("1").performClick()
        composeTestRule.onNodeWithTag("+").performClick()
        composeTestRule.onNodeWithTag("2").performClick()
        composeTestRule.onNodeWithTag("=").performClick()
        composeTestRule.onNodeWithTag("calculatorDisplay").assertTextEquals("3")
    }

    @Test
    fun clearResetsToZero() {
        composeTestRule.onNodeWithTag("9").performClick()
        composeTestRule.onNodeWithTag("C").performClick()
        composeTestRule.onNodeWithTag("calculatorDisplay").assertTextEquals("0")
    }

    @Test
    fun divideByZeroShowsAnError() {
        composeTestRule.onNodeWithTag("5").performClick()
        composeTestRule.onNodeWithTag("÷").performClick()
        composeTestRule.onNodeWithTag("0").performClick()
        composeTestRule.onNodeWithTag("=").performClick()
        composeTestRule.onNodeWithTag("calculatorDisplay").assertTextEquals("Cannot divide by zero")
    }

    @Test
    fun copyButtonIsPresent() {
        composeTestRule.onNodeWithContentDescription("Copy result").assertIsDisplayed()
    }
}
