package com.wcupa.assignmentNotifier.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Assignment] from a given data source.
 */
interface AssignmentRepository {
    /**
     * Retrieve all the assignments from the the given data source.
     */
    fun getAllAssignmentsStream(): Flow<List<Assignment>>

    /**
     * Retrieve an assignment from the given data source that matches with the [id].
     */
    fun getAssignmentStream(id: Int): Flow<Assignment?>

    /**
     * Insert assignment in the data source
     */
    suspend fun insertAssignment(assignment: Assignment)

    /**
     * Delete assignment from the data source
     */
    suspend fun deleteAssignment(assignment: Assignment)

    /**
     * Update assignment in the data source
     */
    suspend fun updateAssignment(assignment: Assignment)

}
