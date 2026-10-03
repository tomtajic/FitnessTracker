package com.fitness.tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.data.model.WorkoutSession
import com.fitness.tracker.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    private val _selectedWorkout = MutableStateFlow<Workout?>(null)
    val selectedWorkout: StateFlow<Workout?> = _selectedWorkout.asStateFlow()

    private val _lastKg = MutableStateFlow(0.0)
    val lastKg: StateFlow<Double> = _lastKg.asStateFlow()

    private val _lastReps = MutableStateFlow(0)
    val lastReps: StateFlow<Int> = _lastReps.asStateFlow()

    private val _currentKg = MutableStateFlow(0.0)
    val currentKg: StateFlow<Double> = _currentKg.asStateFlow()

    private val _currentReps = MutableStateFlow(0)
    val currentReps: StateFlow<Int> = _currentReps.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            repository.getAllWorkouts().collect { _workouts.value = it }
        }
    }

    fun selectWorkout(workout: Workout) {
        _selectedWorkout.value = workout
        viewModelScope.launch {
            val lastSession = repository.getLastSession(workout.id)
            if (lastSession != null) {
                _lastKg.value = lastSession.kg
                _lastReps.value = lastSession.reps
                _currentKg.value = lastSession.kg
                _currentReps.value = lastSession.reps
            } else {
                _lastKg.value = 0.0
                _lastReps.value = 0
                _currentKg.value = 0.0
                _currentReps.value = 0
            }
        }
    }

    fun setCurrentKg(kg: Double) {
        _currentKg.value = kg
    }

    fun setCurrentReps(reps: Int) {
        _currentReps.value = reps
    }

    fun saveSession() {
        val workout = _selectedWorkout.value ?: return
        if (_currentKg.value <= 0.0 || _currentReps.value <= 0) return

        viewModelScope.launch {
            val session = WorkoutSession(
                workoutId = workout.id,
                kg = _currentKg.value,
                reps = _currentReps.value
            )
            repository.saveWorkoutSession(session)
            
            _lastKg.value = _currentKg.value
            _lastReps.value = _currentReps.value
        }
    }

    fun clearSelection() {
        _selectedWorkout.value = null
        _currentKg.value = 0.0
        _currentReps.value = 0
    }
}
