package com.numconversion.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConverterScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mmToInMatchesTheDirectiveExample() {
        composeTestRule.onNodeWithText("Convert").performClick()
        // Default source unit is mm, target is In.
        composeTestRule.onNodeWithTag("2").performClick()
        composeTestRule.onNodeWithTag("5").performClick()
        composeTestRule.onNodeWithTag(".").performClick()
        composeTestRule.onNodeWithTag("4").performClick()
        composeTestRule.onNodeWithTag("converterResult").assertTextEquals("1 in")
    }

    @Test
    fun switchingSourceToFtInShowsTwoInputBoxes() {
        composeTestRule.onNodeWithText("Convert").performClick()
        composeTestRule.onNodeWithText("mm").performClick()
        composeTestRule.onNodeWithText("Ft In").performClick()
        composeTestRule.onNodeWithText("Feet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inches").assertIsDisplayed()
    }
}
