package com.example.testingdemo

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCounterIncrement(assetIsDisplayed: SemanticsNodeInteraction.() -> Unit)
    {
        composeTestRule.setContent { Counter() }
        composeTestRule.onNodeWithTag("CountDisplay").assertTextEquals("Count: 0")
        composeTestRule.onNodeWithText("Add Count").performClick()
        composeTestRule.onNodeWithTag("CountDisplay").assertTextEquals("Count: 1")
        composeTestRule.onNodeWithText("Add Count").performClick()
        composeTestRule.onNodeWithTag("CountDisplay").assertTextEquals("Count: 2")
    }
}