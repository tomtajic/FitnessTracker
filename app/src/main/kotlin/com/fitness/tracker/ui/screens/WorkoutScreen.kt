package com.fitness.tracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.viewmodel.WorkoutViewModel

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel) {
    val workouts by viewModel.workouts.collectAsState()
    val selectedWorkout by viewModel.selectedWorkout.collectAsState()
    val lastKg by viewModel.lastKg.collectAsState()
    val lastReps by viewModel.lastReps.collectAsState()
    val currentKg by viewModel.currentKg.collectAsState()
    val currentReps by viewModel.currentReps.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (selectedWorkout == null) {
            Text(
                "Select a Workout",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            if (workouts.isEmpty()) {
                Text("No workouts available. Create one in Manage tab.")
            } else {
                LazyColumn {
                    items(workouts) { workout ->
                        WorkoutCard(
                            workout = workout,
                            onClick = { viewModel.selectWorkout(workout) }
                        )
                    }
                }
            }
        } else {
            WorkoutDetailView(
                workout = selectedWorkout!!,
                lastKg = lastKg,
                lastReps = lastReps,
                currentKg = currentKg,
                currentReps = currentReps,
                onKgChange = { viewModel.setCurrentKg(it) },
                onRepsChange = { viewModel.setCurrentReps(it) },
                onSave = { viewModel.saveSession() },
                onBack = { viewModel.clearSelection() }
            )
        }
    }
}

@Composable
fun WorkoutCard(workout: Workout, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                workout.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (workout.description.isNotEmpty()) {
                Text(
                    workout.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun WorkoutDetailView(
    workout: Workout,
    lastKg: Double,
    lastReps: Int,
    currentKg: Double,
    currentReps: Int,
    onKgChange: (Double) -> Unit,
    onRepsChange: (Int) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Back")
        }

        Text(
            workout.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (lastKg > 0.0) {
            Text(
                "Last: $lastKg kg x $lastReps reps",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        InputField(
            label = "Weight (kg)",
            value = currentKg.toString(),
            onValueChange = { if (it.isNotEmpty()) onKgChange(it.toDoubleOrNull() ?: 0.0) }
        )

        InputField(
            label = "Reps",
            value = currentReps.toString(),
            onValueChange = { if (it.isNotEmpty()) onRepsChange(it.toIntOrNull() ?: 0) }
        )

        Button(
            onClick = onSave,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp),
            enabled = currentKg > 0 && currentReps > 0
        ) {
            Text("Save Workout", fontSize = 16.sp)
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = true
    )
}
