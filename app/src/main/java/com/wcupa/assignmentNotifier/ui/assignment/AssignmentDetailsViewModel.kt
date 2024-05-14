package com.wcupa.assignmentNotifier.ui.assignment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcupa.assignmentNotifier.data.AssignmentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

//ViewModel to retrieve, update and delete an assignment from the AssignmentRepository's data source.
class AssignmentDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val assignmentRepository: AssignmentRepository,
) : ViewModel() {

    private val assignmentId: Int = checkNotNull(savedStateHandle[AssignmentDetailsDestination.ASSIGNMENT_ID_ARG])

    //Holds the assignment details ui state. The data is retrieved from AssignmentRepository and mapped to the UI state.
    val uiState: StateFlow<AssignmentDetailsUiState> =
        assignmentRepository.getAssignmentStream(assignmentId)
            .filterNotNull()
            .map {
                AssignmentDetailsUiState(assignmentDetails = it.toAssignmentDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AssignmentDetailsUiState()
            )

    //Deletes the assignment from the AssignmentRepository's data source.
    suspend fun deleteAssignment() {
        assignmentRepository.deleteAssignment(uiState.value.assignmentDetails.toAssignment())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

//UI state for AssignmentDetailsScreen
data class AssignmentDetailsUiState(
    val assignmentDetails: AssignmentDetails = AssignmentDetails()
)
