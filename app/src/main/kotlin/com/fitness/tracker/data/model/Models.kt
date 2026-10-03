package com.fitness.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val videoLink: String = "",
    val imagePath: String = ""
)

@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workoutId: Int,
    val kg: Double,
    val reps: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class WorkoutWithLastUsed(
    val workout: Workout,
    val lastKg: Double = 0.0,
    val lastReps: Int = 0
)

data class WorkoutStats(
    val workoutId: Int,
    val workoutName: String,
    val totalSessions: Int = 0,
    val totalKg: Double = 0.0,
    val avgKg: Double = 0.0,
    val totalReps: Int = 0,
    val avgReps: Double = 0.0,
    val maxKg: Double = 0.0,
    val maxReps: Int = 0
)
