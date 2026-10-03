package com.fitness.tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.model.WorkoutSession
import com.fitness.tracker.data.model.WorkoutStats
import com.fitness.tracker.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _dailyStats = MutableStateFlow<Map<Int, WorkoutStats>>(emptyMap())
    val dailyStats: StateFlow<Map<Int, WorkoutStats>> = _dailyStats.asStateFlow()

    private val _weeklyStats = MutableStateFlow<Map<Int, WorkoutStats>>(emptyMap())
    val weeklyStats: StateFlow<Map<Int, WorkoutStats>> = _weeklyStats.asStateFlow()

    private val _monthlyStats = MutableStateFlow<Map<Int, WorkoutStats>>(emptyMap())
    val monthlyStats: StateFlow<Map<Int, WorkoutStats>> = _monthlyStats.asStateFlow()

    private val _dailySessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val dailySessions: StateFlow<List<WorkoutSession>> = _dailySessions.asStateFlow()

    private val _weeklySessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val weeklySessions: StateFlow<List<WorkoutSession>> = _weeklySessions.asStateFlow()

    private val _monthlySessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val monthlySessions: StateFlow<List<WorkoutSession>> = _monthlySessions.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            val dailySessions = repository.getSessionsForToday()
            _dailySessions.value = dailySessions
            _dailyStats.value = repository.getStatsForPeriod(dailySessions)

            val weeklySessions = repository.getSessionsForWeek()
            _weeklySessions.value = weeklySessions
            _weeklyStats.value = repository.getStatsForPeriod(weeklySessions)

            val monthlySessions = repository.getSessionsForMonth()
            _monthlySessions.value = monthlySessions
            _monthlyStats.value = repository.getStatsForPeriod(monthlySessions)
        }
    }

    fun refreshStats() {
        loadStats()
    }
}
