package com.wcupa.assignmentNotifier.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val assignmentRepository: AssignmentRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineAssignmentRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AssignmentRepository]
     */
    override val assignmentRepository: AssignmentRepository by lazy {
        OfflineAssignmentRepository(AssignmentDatabase.getDatabase(context).assignmentDao())
    }
}
