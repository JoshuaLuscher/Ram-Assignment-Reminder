package com.wcupa.assignmentNotifier.ui.assignment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wcupa.assignmentNotifier.data.Assignment
import com.wcupa.assignmentNotifier.data.AssignmentRepository
import java.util.Date

//ViewModel to validate and insert assignments in the Room database.
class AssignmentEntryViewModel(private val assignmentRepository: AssignmentRepository) : ViewModel() {

    //Holds current assignment ui state
    var assignmentUiState by mutableStateOf(AssignmentUiState())
        private set

    // Updates the assignmentUiState with the value provided in the argument. This method also triggers a validation for input values.
    fun updateUiState(assignmentDetails: AssignmentDetails) {
        assignmentUiState =
            AssignmentUiState(assignmentDetails = assignmentDetails, isEntryValid = validateInput(assignmentDetails))
    }

    //Inserts an [Assignment] into the Room database
    suspend fun saveAssignment() {
        if (validateInput()) {
            assignmentRepository.insertAssignment(assignmentUiState.assignmentDetails.toAssignment())
        }
    }

    private fun validateInput(uiState: AssignmentDetails = assignmentUiState.assignmentDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && course.isNotBlank() && dueDate.toString().isNotBlank()
        }
    }
}

//Represents Ui State for an [Assignment].
data class AssignmentUiState(
    val assignmentDetails: AssignmentDetails = AssignmentDetails(),
    val isEntryValid: Boolean = false
)

data class AssignmentDetails(
    val id: Int = 0,
    val name: String = "",
    val dueDate: Date = Date(),
    val course: String = "",
    var notified: Boolean = false
)

//Extension function to convert [AssignmentUiState] to [Assignment]
fun AssignmentDetails.toAssignment(): Assignment = Assignment(
    id = id,
    name = name,
    dueDate = dueDate,
    course = course,
    notified = notified
)

//Extension function to convert [Assignment] to [AssignmentUiState]
fun Assignment.toAssignmentUiState(isEntryValid: Boolean = false): AssignmentUiState = AssignmentUiState(
    assignmentDetails = this.toAssignmentDetails(),
    isEntryValid = isEntryValid
)

//Extension function to convert [Assignment] to [AssignmentDetails]
fun Assignment.toAssignmentDetails(): AssignmentDetails = AssignmentDetails(
    id = id,
    name = name,
    dueDate = dueDate,
    course = course,
    notified = notified
)
