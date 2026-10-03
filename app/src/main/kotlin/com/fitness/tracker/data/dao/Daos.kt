package com.fitness.tracker.data.dao

import androidx.room.*
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.data.model.WorkoutSession
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workouts ORDER BY name ASC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWorkoutById(id: Int): Workout?
}

@Dao
interface WorkoutSessionDao {
    @Insert
    suspend fun insertSession(session: WorkoutSession): Long

    @Delete
    suspend fun deleteSession(session: WorkoutSession)

    @Query("SELECT * FROM workout_sessions WHERE workoutId = :workoutId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastSession(workoutId: Int): WorkoutSession?

    @Query("SELECT * FROM workout_sessions WHERE workoutId = :workoutId ORDER BY timestamp DESC")
    fun getSessionsByWorkout(workoutId: Int): Flow<List<WorkoutSession>>

    @Query("""
        SELECT * FROM workout_sessions 
        WHERE timestamp >= :startTime AND timestamp <= :endTime
        ORDER BY timestamp DESC
    """)
    suspend fun getSessionsBetween(startTime: Long, endTime: Long): List<WorkoutSession>

    @Query("SELECT * FROM workout_sessions ORDER BY timestamp DESC")
    suspend fun getAllSessions(): List<WorkoutSession>
}
