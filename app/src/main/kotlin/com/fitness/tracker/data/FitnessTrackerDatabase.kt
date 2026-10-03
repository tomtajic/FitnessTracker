package com.fitness.tracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fitness.tracker.data.dao.WorkoutDao
import com.fitness.tracker.data.dao.WorkoutSessionDao
import com.fitness.tracker.data.model.Workout
import com.fitness.tracker.data.model.WorkoutSession

@Database(entities = [Workout::class, WorkoutSession::class], version = 1)
abstract class FitnessTrackerDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutSessionDao(): WorkoutSessionDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessTrackerDatabase? = null

        fun getDatabase(context: Context): FitnessTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessTrackerDatabase::class.java,
                    "fitness_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
