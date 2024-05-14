package com.wcupa.assignmentNotifier.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wcupa.assignmentNotifier.data.Assignment
import com.wcupa.assignmentNotifier.data.AssignmentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

//ViewModel to retrieve all assignments in the Room database.
class HomeViewModel(assignmentRepository: AssignmentRepository) : ViewModel() {

    //Holds home ui state. The list of assignments are retrieved from AssignmentRepository and mapped to HomeUiState
    val homeUiState: StateFlow<HomeUiState> =
        assignmentRepository.getAllAssignmentsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

//Ui State for HomeScreen
data class HomeUiState(val assignmentList: List<Assignment> = listOf())
