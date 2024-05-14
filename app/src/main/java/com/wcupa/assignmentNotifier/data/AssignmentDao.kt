package com.wcupa.assignmentNotifier.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the database
 */
@Dao
interface AssignmentDao {

    @Query("SELECT * from assignments ORDER BY name ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Query("SELECT * from assignments WHERE id = :id")
    fun getAssignment(id: Int): Flow<Assignment>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Assignment into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(assignment: Assignment)

    @Update
    suspend fun update(assignment: Assignment)

    @Delete
    suspend fun delete(assignment: Assignment)
}
