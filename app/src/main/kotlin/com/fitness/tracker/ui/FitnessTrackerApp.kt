package com.fitness.tracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.fitness.tracker.ui.screens.ManageScreen
import com.fitness.tracker.ui.screens.StatsScreen
import com.fitness.tracker.ui.screens.WorkoutScreen
import com.fitness.tracker.viewmodel.ManageViewModel
import com.fitness.tracker.viewmodel.StatsViewModel
import com.fitness.tracker.viewmodel.WorkoutViewModel

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Workout : BottomNavItem("workout", Icons.Filled.Favorite, "Workout")
    object Stats : BottomNavItem("stats", Icons.Filled.ThumbUp, "Stats")
    object Manage : BottomNavItem("manage", Icons.Filled.Settings, "Manage")
}

@Composable
fun FitnessTrackerApp(
    workoutViewModel: WorkoutViewModel,
    statsViewModel: StatsViewModel,
    manageViewModel: ManageViewModel
) {
    var selectedTab by remember { mutableStateOf("workout") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(BottomNavItem.Workout.icon, contentDescription = null) },
                    label = { Text(BottomNavItem.Workout.label) },
                    selected = selectedTab == "workout",
                    onClick = { selectedTab = "workout" }
                )
                NavigationBarItem(
                    icon = { Icon(BottomNavItem.Stats.icon, contentDescription = null) },
                    label = { Text(BottomNavItem.Stats.label) },
                    selected = selectedTab == "stats",
                    onClick = { selectedTab = "stats" }
                )
                NavigationBarItem(
                    icon = { Icon(BottomNavItem.Manage.icon, contentDescription = null) },
                    label = { Text(BottomNavItem.Manage.label) },
                    selected = selectedTab == "manage",
                    onClick = { selectedTab = "manage" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "workout" -> WorkoutScreen(workoutViewModel)
                "stats" -> StatsScreen(statsViewModel)
                "manage" -> ManageScreen(manageViewModel)
            }
        }
    }
}
