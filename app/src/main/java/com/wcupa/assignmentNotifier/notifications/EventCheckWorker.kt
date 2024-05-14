package com.wcupa.assignmentNotifier.notifications

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wcupa.assignmentNotifier.data.AssignmentDatabase
import kotlinx.coroutines.runBlocking
import java.util.Calendar

//Used Check if notifications need to be sent out
class EventCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("START doWork", "START doWork")
        return try {
            // Retrieve all events from Assignment
            val assignmentsFlow = AssignmentDatabase.getDatabase(applicationContext).assignmentDao()
                .getAllAssignments()

            val today = Calendar.getInstance().timeInMillis

            runBlocking {
                //Converts Flow<List<Assignment>> into List<Assignment>
                assignmentsFlow.collect { assignments ->
                    // Check if any event date is today. If today, send a notification
                    for (assignment in assignments) {
                        Log.d("Loop doWork", "Loop doWork for id " + assignment.id.toString())

                        //Sends out notification if dueDate is in 1 day or less AND a notification hasn't already been sent for this assignment
                        val dayBeforeDue = assignment.dueDate.time - 86400000
                        Log.d("dayBefore", dayBeforeDue.toString())
                        Log.d("Today", today.toString())
                        if (dayBeforeDue <= today && !assignment.notified) {
                            sendNotification(applicationContext, assignment.name, assignment.course, assignment.id)

                            //Sets notified to true, making sure notifications don't repeat
                            assignment.notified = true
                            AssignmentDatabase.getDatabase(applicationContext).assignmentDao().update(assignment)
                        }
                    }
                }
            }

            Log.d("SUCCESS doWork", "SUCCESS doWork")
            Result.success()
        } catch (e: Exception) {
            // Log the exception
            Log.e("EventCheckWorker", "Error processing events", e)
            Result.failure()
        }
    }
}