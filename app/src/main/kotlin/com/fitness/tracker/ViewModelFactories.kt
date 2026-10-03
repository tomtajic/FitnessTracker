package com.fitness.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fitness.tracker.repository.WorkoutRepository
import com.fitness.tracker.viewmodel.ManageViewModel
import com.fitness.tracker.viewmodel.StatsViewModel
import com.fitness.tracker.viewmodel.WorkoutViewModel

class WorkoutViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WorkoutViewModel(repository) as T
    }
}

class StatsViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatsViewModel(repository) as T
    }
}

class ManageViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageViewModel(repository) as T
    }
}
