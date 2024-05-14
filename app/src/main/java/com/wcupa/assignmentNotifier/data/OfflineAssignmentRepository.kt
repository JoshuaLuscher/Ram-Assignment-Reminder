package com.wcupa.assignmentNotifier.data

import kotlinx.coroutines.flow.Flow

//Preforms insert, update, delete, and retrieval of data from our database
class OfflineAssignmentRepository(private val assignmentDao: AssignmentDao) : AssignmentRepository {
    override fun getAllAssignmentsStream(): Flow<List<Assignment>> = assignmentDao.getAllAssignments()

    override fun getAssignmentStream(id: Int): Flow<Assignment?> = assignmentDao.getAssignment(id)

    override suspend fun insertAssignment(assignment: Assignment) = assignmentDao.insert(assignment)

    override suspend fun deleteAssignment(assignment: Assignment) = assignmentDao.delete(assignment)

    override suspend fun updateAssignment(assignment: Assignment) = assignmentDao.update(assignment)
}
