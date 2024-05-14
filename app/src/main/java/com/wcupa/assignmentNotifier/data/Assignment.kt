package com.wcupa.assignmentNotifier.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


//Assignment class to hold id and input values for each data entry
@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dueDate: Date,
    val course: String,
    var notified: Boolean = false
)
