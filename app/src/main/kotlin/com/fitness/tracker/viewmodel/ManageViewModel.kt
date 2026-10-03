package com.fitness.tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManageViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    private val _editingWorkout = MutableStateFlow<Workout?>(null)
    val editingWorkout: StateFlow<Workout?> = _editingWorkout.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            repository.getAllWorkouts().collect { _workouts.value = it }
        }
    }

    fun startEditingWorkout(workout: Workout) {
        _editingWorkout.value = workout.copy()
    }

    fun cancelEditing() {
        _editingWorkout.value = null
    }

    fun saveWorkout(workout: Workout) {
        viewModelScope.launch {
            if (workout.id == 0) {
                repository.insertWorkout(workout)
            } else {
                repository.updateWorkout(workout)
            }
            _editingWorkout.value = null
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    fun createNewWorkout() {
        _editingWorkout.value = Workout(
            id = 0,
            name = "",
            description = "",
            videoLink = "",
            imagePath = ""
        )
    }
}
