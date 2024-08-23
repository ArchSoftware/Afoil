package com.archsoftware.afoil.core.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.archsoftware.afoil.core.model.ComputationLog
import com.archsoftware.afoil.core.testing.util.assertTextIsOfColor
import org.junit.Rule
import org.junit.Test

class ComputationLogRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test data
    private val warningLevelLog = ComputationLog(
        timestamp = "2023-01-01 12:00:00",
        tag = "ComputationLog",
        message = "This is a computation log message.",
        level = ComputationLog.Level.WARNING
    )
    private val errorLevelLog = ComputationLog(
        timestamp = "2023-01-01 12:00:00",
        tag = "ComputationLog",
        message = "This is a computation log message.",
        level = ComputationLog.Level.ERROR
    )
    private val infoLevelLog = ComputationLog(
        timestamp = "2023-01-01 12:00:00",
        tag = "ComputationLog",
        message = "This is a computation log message.",
        level = ComputationLog.Level.INFO
    )


    @Test
    fun computationLogRow_logMessageIsColoredForWarningLevel() {
        composeTestRule.setContent {
            ComputationLogRow(log = warningLevelLog)
        }

        composeTestRule
            .onNodeWithText(warningLevelLog.message)
            .assertTextIsOfColor(Color(ComputationLog.Level.WARNING.color))
    }

    @Test
    fun computationLogRow_logMessageIsColoredForErrorLevel() {
        composeTestRule.setContent {
            ComputationLogRow(log = errorLevelLog)
        }

        composeTestRule
            .onNodeWithText(errorLevelLog.message)
            .assertTextIsOfColor(Color(ComputationLog.Level.ERROR.color))
    }

    @Test
    fun computationLogRow_logMessageIsNotColoredForInfoLevel() {
        composeTestRule.setContent {
            ComputationLogRow(log = infoLevelLog)
        }

        composeTestRule
            .onNodeWithText(infoLevelLog.message)
            .assertTextIsOfColor(Color.Black)
    }
}