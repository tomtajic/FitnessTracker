package com.fitness.tracker

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.ui.screens.WorkoutCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun workoutCardDisplaysCorrectly() {
        val workout = Workout(
            id = 1,
            name = "Bench Press",
            description = "Chest workout"
        )
        
        var clicked = false
        composeTestRule.setContent {
            WorkoutCard(
                workout = workout,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText("Bench Press").assertExists()
        composeTestRule.onNodeWithText("Chest workout").assertExists()
        
        composeTestRule.onNodeWithText("Bench Press").performClick()
        assert(clicked)
    }
}
