@file:Suppress("DEPRECATION")

package com.wcupa.assignmentNotifier.ui.assignment

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcupa.assignmentNotifier.DateUtils
import com.wcupa.assignmentNotifier.R
import com.wcupa.assignmentNotifier.TopAppBar
import com.wcupa.assignmentNotifier.ui.AppViewModelProvider
import com.wcupa.assignmentNotifier.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


object AssignmentEntryDestination : NavigationDestination, ComponentActivity()  {
    override val route = "assignment_entry"
    override val titleRes = R.string.assignment_entry_title
}

//Manages layout of the assignment entry screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AssignmentEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    //Title text
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(AssignmentEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
        //Body with data entry fields and save button
    ) { innerPadding ->
        AssignmentEntryBody(
            assignmentUiState = viewModel.assignmentUiState,
            onAssignmentValueChange = viewModel::updateUiState,
            //Save button
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveAssignment()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

//Body function to create field entry boxes and save button
@Composable
fun AssignmentEntryBody(
    assignmentUiState: AssignmentUiState,
    onAssignmentValueChange: (AssignmentDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        //Name and due date inputs
        AssignmentInputForm(
            assignmentDetails = assignmentUiState.assignmentDetails,
            onValueChange = onAssignmentValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        //Save button
        Button(
            onClick = onSaveClick,
            enabled = assignmentUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

//Name and due date input forums
@Composable
fun AssignmentInputForm(
    assignmentDetails: AssignmentDetails,
    modifier: Modifier = Modifier,
    onValueChange: (AssignmentDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        //Name forum
        Text(text = "Assignment")
        OutlinedTextField(
            value = assignmentDetails.name,
            onValueChange = { onValueChange(assignmentDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.assignment_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        //Course
        Text(text = "Class")
        OutlinedTextField(
            value = assignmentDetails.course,
            onValueChange = { onValueChange(assignmentDetails.copy(course = it)) },
            label = { Text(stringResource(R.string.assignment_course_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        //Date Picker button.
        DatePickerWithDialog(
            assignmentDetails = assignmentDetails,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth())
    }
}

//Date Picker Functionality
@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithDialog(
    modifier: Modifier = Modifier,
    assignmentDetails: AssignmentDetails,
    onValueChange: (AssignmentDetails) -> Unit = {},
) {
    val dateState = rememberDatePickerState()
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    //Assigns text on date picker button
    val formatter = SimpleDateFormat("E, dd MMMM, yyyy")
    val dateToString = millisToLocalDate?.let {
        DateUtils().dateToString(millisToLocalDate)
    } ?: formatter.format((System.currentTimeMillis())).toString()

    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Date Picker button
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = dateToString,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                //OK button in date picker
                confirmButton = {
                    Button(
                        onClick = { showDialog = false
                            // Convert LocalDate to Date
                            val dueDate = millisToLocalDate?.let { DateUtils().localDateToDate(it) } ?: Date()
                            // Update dueDate in AssignmentDetails + Reset notified to false in case dueDate is changed after notified is set to true
                            dueDate.hours += 4
                            val updatedAssignmentDetails = assignmentDetails.copy(dueDate = dueDate, notified = false)
                        onValueChange(updatedAssignmentDetails)
                        }
                    ) {
                        Text(text = "OK")
                    }
                },
                //Cancel button in date picker
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = dateState,
                    showModeToggle = true
                )
            }
        }
    }
}
