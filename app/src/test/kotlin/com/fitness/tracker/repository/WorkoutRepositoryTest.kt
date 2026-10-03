package com.fitness.tracker.repository

import com.fitness.tracker.data.dao.WorkoutDao
import com.fitness.tracker.data.dao.WorkoutSessionDao
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.data.model.WorkoutSession
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class WorkoutRepositoryTest {
    @Mock
    private lateinit var workoutDao: WorkoutDao
    
    @Mock
    private lateinit var workoutSessionDao: WorkoutSessionDao
    
    private lateinit var repository: WorkoutRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = WorkoutRepository(workoutDao, workoutSessionDao)
    }

    @Test
    fun getAllWorkoutsReturnsWorkoutFlow() = runBlocking {
        val workouts = listOf(
            Workout(1, "Bench Press"),
            Workout(2, "Squats")
        )
        
        whenever(workoutDao.getAllWorkouts()).thenReturn(flowOf(workouts))
        
        var result: List<Workout>? = null
        repository.getAllWorkouts().collect { result = it }
        
        assertEquals(2, result?.size)
        assertEquals("Bench Press", result?.get(0)?.name)
    }

    @Test
    fun getLastSessionReturnsCorrectSession() = runBlocking {
        val session = WorkoutSession(1, 1, 100.0, 10)
        
        whenever(workoutSessionDao.getLastSession(1)).thenReturn(session)
        
        val result = repository.getLastSession(1)
        
        assertEquals(100.0, result?.kg)
        assertEquals(10, result?.reps)
    }

    @Test
    fun insertWorkoutCallsDao() = runBlocking {
        val workout = Workout(name = "Deadlift")
        
        whenever(workoutDao.insertWorkout(workout)).thenReturn(1L)
        
        val result = repository.insertWorkout(workout)
        
        assertEquals(1L, result)
    }
}
