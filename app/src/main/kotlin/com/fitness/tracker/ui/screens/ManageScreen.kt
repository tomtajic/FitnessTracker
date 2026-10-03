package com.fitness.tracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.viewmodel.ManageViewModel

@Composable
fun ManageScreen(viewModel: ManageViewModel) {
    val workouts by viewModel.workouts.collectAsState()
    val editingWorkout by viewModel.editingWorkout.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Manage Workouts",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            FloatingActionButton(
                onClick = { viewModel.createNewWorkout() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Workout")
            }
        }

        if (editingWorkout != null) {
            WorkoutEditForm(
                workout = editingWorkout!!,
                onSave = { viewModel.saveWorkout(it) },
                onCancel = { viewModel.cancelEditing() }
            )
        } else {
            if (workouts.isEmpty()) {
                Text(
                    "No workouts yet. Click + to add one.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 24.dp)
                )
            } else {
                LazyColumn {
                    items(workouts) { workout ->
                        ManageWorkoutCard(
                            workout = workout,
                            onEdit = { viewModel.startEditingWorkout(workout) },
                            onDelete = { viewModel.deleteWorkout(workout) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ManageWorkoutCard(
    workout: Workout,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
            if (workout.videoLink.isNotEmpty()) {
                Text(
                    "Video: ${workout.videoLink}",
                    fontSize = 12.sp,
                    color = Color.Blue,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun WorkoutEditForm(
    workout: Workout,
    onSave: (Workout) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(workout.name) }
    var description by remember { mutableStateOf(workout.description) }
    var videoLink by remember { mutableStateOf(workout.videoLink) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            if (workout.id == 0) "New Workout" else "Edit Workout",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Workout Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            maxLines = 3
        )

        OutlinedTextField(
            value = videoLink,
            onValueChange = { videoLink = it },
            label = { Text("Video Link (URL)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        onSave(
                            workout.copy(
                                name = name,
                                description = description,
                                videoLink = videoLink
                            )
                        )
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = name.isNotEmpty()
            ) {
                Text("Save")
            }
        }
    }
}
