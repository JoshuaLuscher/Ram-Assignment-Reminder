package com.wcupa.assignmentNotifier.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wcupa.assignmentNotifier.Converters

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Assignment::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AssignmentDatabase : RoomDatabase() {
    abstract fun assignmentDao(): AssignmentDao

    companion object {
        @Volatile
        private var Instance: AssignmentDatabase? = null

        fun getDatabase(context: Context): AssignmentDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AssignmentDatabase::class.java, "assignment_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }


    }
}
