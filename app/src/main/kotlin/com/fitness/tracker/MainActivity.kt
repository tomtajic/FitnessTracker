package com.fitness.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import com.fitness.tracker.data.FitnessTrackerDatabase
import com.fitness.tracker.repository.WorkoutRepository
import com.fitness.tracker.ui.FitnessTrackerApp
import com.fitness.tracker.viewmodel.ManageViewModel
import com.fitness.tracker.viewmodel.StatsViewModel
import com.fitness.tracker.viewmodel.WorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = FitnessTrackerDatabase.getDatabase(applicationContext)
        val repository = WorkoutRepository(
            database.workoutDao(),
            database.workoutSessionDao()
        )

        val workoutViewModelFactory = WorkoutViewModelFactory(repository)
        val statsViewModelFactory = StatsViewModelFactory(repository)
        val manageViewModelFactory = ManageViewModelFactory(repository)

        val workoutViewModel = ViewModelProvider(
            this,
            workoutViewModelFactory
        )[WorkoutViewModel::class.java]

        val statsViewModel = ViewModelProvider(
            this,
            statsViewModelFactory
        )[StatsViewModel::class.java]

        val manageViewModel = ViewModelProvider(
            this,
            manageViewModelFactory
        )[ManageViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface {
                    FitnessTrackerApp(
                        workoutViewModel = workoutViewModel,
                        statsViewModel = statsViewModel,
                        manageViewModel = manageViewModel
                    )
                }
            }
        }
    }
}
