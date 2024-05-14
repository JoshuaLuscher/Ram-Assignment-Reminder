package com.wcupa.assignmentNotifier.ui.assignment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcupa.assignmentNotifier.data.AssignmentRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//ViewModel to retrieve and update an assignment from the AssignmentRepository's data source.
class AssignmentEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val assignmentRepository: AssignmentRepository
) : ViewModel() {

    //Holds current assignment ui state
    var assignmentUiState by mutableStateOf(AssignmentUiState())
        private set

    private val assignmentId: Int = checkNotNull(savedStateHandle[AssignmentEditDestination.ASSIGNMENT_ID_ARG])

    init {
        viewModelScope.launch {
            assignmentUiState = assignmentRepository.getAssignmentStream(assignmentId)
                .filterNotNull()
                .first()
                .toAssignmentUiState(true)
        }
    }


    //Update the assignment in the AssignmentRepository's data source
    suspend fun updateAssignment() {
        if (validateInput(assignmentUiState.assignmentDetails)) {
            assignmentRepository.updateAssignment(assignmentUiState.assignmentDetails.toAssignment())
        }
    }

    //Updates the [assignmentUiState] with the value provided in the argument. This method also triggers a validation for input values.
    fun updateUiState(assignmentDetails: AssignmentDetails) {
        assignmentUiState =
            AssignmentUiState(assignmentDetails = assignmentDetails, isEntryValid = validateInput(assignmentDetails))
    }

    //Confirms that input is not empty
    private fun validateInput(uiState: AssignmentDetails = assignmentUiState.assignmentDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && dueDate.toString().isNotBlank()
        }
    }
}
