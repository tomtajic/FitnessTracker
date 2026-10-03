package com.fitness.tracker

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fitness.tracker.data.FitnessTrackerDatabase
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.data.model.WorkoutSession
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: FitnessTrackerDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FitnessTrackerDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun canInsertAndRetrieveWorkout() = runBlocking {
        val workout = Workout(
            name = "Bench Press",
            description = "Upper body strength",
            videoLink = "https://youtube.com/...",
            imagePath = ""
        )
        val id = database.workoutDao().insertWorkout(workout)
        val retrieved = database.workoutDao().getWorkoutById(id.toInt())
        
        assertNotNull(retrieved)
        assertEquals("Bench Press", retrieved?.name)
    }

    @Test
    fun canInsertAndRetrieveWorkoutSession() = runBlocking {
        val workout = Workout(name = "Squats")
        val workoutId = database.workoutDao().insertWorkout(workout).toInt()
        
        val session = WorkoutSession(
            workoutId = workoutId,
            kg = 100.0,
            reps = 10
        )
        database.workoutSessionDao().insertSession(session)
        
        val lastSession = database.workoutSessionDao().getLastSession(workoutId)
        assertNotNull(lastSession)
        assertEquals(100.0, lastSession?.kg)
        assertEquals(10, lastSession?.reps)
    }

    @Test
    fun lastSessionPrefilsCorrectly() = runBlocking {
        val workout = Workout(name = "Deadlift")
        val workoutId = database.workoutDao().insertWorkout(workout).toInt()
        
        // First session
        val session1 = WorkoutSession(
            workoutId = workoutId,
            kg = 120.0,
            reps = 5
        )
        database.workoutSessionDao().insertSession(session1)
        
        // Second session with different weight
        val session2 = WorkoutSession(
            workoutId = workoutId,
            kg = 140.0,
            reps = 3
        )
        database.workoutSessionDao().insertSession(session2)
        
        val lastSession = database.workoutSessionDao().getLastSession(workoutId)
        assertNotNull(lastSession)
        assertEquals(140.0, lastSession?.kg)  // Should be most recent
        assertEquals(3, lastSession?.reps)
    }

    @Test
    fun canUpdateWorkout() = runBlocking {
        val workout = Workout(
            id = 1,
            name = "Push-ups",
            description = "Original description"
        )
        database.workoutDao().insertWorkout(workout)
        
        val updated = workout.copy(description = "Updated description")
        database.workoutDao().updateWorkout(updated)
        
        val retrieved = database.workoutDao().getWorkoutById(1)
        assertEquals("Updated description", retrieved?.description)
    }

    @Test
    fun canDeleteWorkout() = runBlocking {
        val workout = Workout(name = "Pull-ups")
        val id = database.workoutDao().insertWorkout(workout).toInt()
        
        val retrieved = database.workoutDao().getWorkoutById(id)
        assertNotNull(retrieved)
        
        database.workoutDao().deleteWorkout(retrieved!!)
        val afterDelete = database.workoutDao().getWorkoutById(id)
        
        assertEquals(null, afterDelete)
    }
}
