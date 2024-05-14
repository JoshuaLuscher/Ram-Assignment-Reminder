package com.wcupa.assignmentNotifier.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wcupa.assignmentNotifier.AssignmentApplication
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentDetailsViewModel
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentEditViewModel
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentEntryViewModel
import com.wcupa.assignmentNotifier.ui.home.HomeViewModel

//Provides Factory to create instance of ViewModel for the entire app
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AssignmentEditViewModel
        initializer {
            AssignmentEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.assignmentRepository
            )
        }
        // Initializer for AssignmentEntryViewModel
        initializer {
            AssignmentEntryViewModel(inventoryApplication().container.assignmentRepository)
        }

        // Initializer for AssignmentDetailsViewModel
        initializer {
            AssignmentDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.assignmentRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(inventoryApplication().container.assignmentRepository)
        }
    }
}

//Extension function to queries for [Application] object and returns an instance of [AssignmentApplication].
fun CreationExtras.inventoryApplication(): AssignmentApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AssignmentApplication)
