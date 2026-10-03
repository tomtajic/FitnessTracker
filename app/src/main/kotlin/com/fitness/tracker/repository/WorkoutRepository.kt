package com.fitness.tracker.repository

import com.fitness.tracker.data.dao.WorkoutDao
import com.fitness.tracker.data.dao.WorkoutSessionDao
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.data.model.WorkoutSession
import com.fitness.tracker.data.model.WorkoutStats
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val workoutSessionDao: WorkoutSessionDao
) {
    fun getAllWorkouts(): Flow<List<Workout>> = workoutDao.getAllWorkouts()

    suspend fun insertWorkout(workout: Workout): Long = workoutDao.insertWorkout(workout)

    suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)

    suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)

    suspend fun getWorkoutById(id: Int): Workout? = workoutDao.getWorkoutById(id)

    suspend fun saveWorkoutSession(session: WorkoutSession): Long = 
        workoutSessionDao.insertSession(session)

    suspend fun getLastSession(workoutId: Int): WorkoutSession? = 
        workoutSessionDao.getLastSession(workoutId)

    fun getSessionsByWorkout(workoutId: Int): Flow<List<WorkoutSession>> = 
        workoutSessionDao.getSessionsByWorkout(workoutId)

    suspend fun getSessionsForToday(): List<WorkoutSession> {
        val now = System.currentTimeMillis()
        val startOfDay = LocalDateTime.now()
            .truncatedTo(ChronoUnit.DAYS)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return workoutSessionDao.getSessionsBetween(startOfDay, now)
    }

    suspend fun getSessionsForWeek(): List<WorkoutSession> {
        val now = System.currentTimeMillis()
        val startOfWeek = LocalDateTime.now()
            .minusDays(7)
            .truncatedTo(ChronoUnit.DAYS)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return workoutSessionDao.getSessionsBetween(startOfWeek, now)
    }

    suspend fun getSessionsForMonth(): List<WorkoutSession> {
        val now = System.currentTimeMillis()
        val startOfMonth = LocalDateTime.now()
            .minusDays(30)
            .truncatedTo(ChronoUnit.DAYS)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return workoutSessionDao.getSessionsBetween(startOfMonth, now)
    }

    suspend fun getStatsForPeriod(sessions: List<WorkoutSession>): Map<Int, WorkoutStats> {
        val allWorkouts = getAllWorkoutsList()
        val stats = mutableMapOf<Int, WorkoutStats>()

        allWorkouts.forEach { workout ->
            val workoutSessions = sessions.filter { it.workoutId == workout.id }
            val totalKg = workoutSessions.sumOf { it.kg }
            val totalReps = workoutSessions.sumOf { it.reps }
            val maxKg = workoutSessions.maxOfOrNull { it.kg } ?: 0.0
            val maxReps = workoutSessions.maxOfOrNull { it.reps } ?: 0

            stats[workout.id] = WorkoutStats(
                workoutId = workout.id,
                workoutName = workout.name,
                totalSessions = workoutSessions.size,
                totalKg = totalKg,
                avgKg = if (workoutSessions.isNotEmpty()) totalKg / workoutSessions.size else 0.0,
                totalReps = totalReps,
                avgReps = if (workoutSessions.isNotEmpty()) totalReps.toDouble() / workoutSessions.size else 0.0,
                maxKg = maxKg,
                maxReps = maxReps
            )
        }

        return stats
    }

    private suspend fun getAllWorkoutsList(): List<Workout> {
        val list = mutableListOf<Workout>()
        getAllWorkouts().collect { list.addAll(it) }
        return list
    }

    suspend fun getWorkoutProgressionForChart(
        workoutId: Int,
        sessions: List<WorkoutSession>
    ): List<Pair<String, Double>> {
        val workoutSessions = sessions.filter { it.workoutId == workoutId }
            .sortedBy { it.timestamp }
            .takeLast(10) // Last 10 sessions for chart readability

        return workoutSessions.mapIndexed { index, session ->
            Pair("Session ${index + 1}", session.kg)
        }
    }
}
