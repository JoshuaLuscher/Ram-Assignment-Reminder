package com.wcupa.assignmentNotifier.ui.assignment

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcupa.assignmentNotifier.R
import com.wcupa.assignmentNotifier.TopAppBar
import com.wcupa.assignmentNotifier.data.Assignment
import com.wcupa.assignmentNotifier.ui.AppViewModelProvider
import com.wcupa.assignmentNotifier.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

//Manages assignment details screen that appears when selecting an individual assignment item from the list
object AssignmentDetailsDestination : NavigationDestination {
    override val route = "assignment_details"
    override val titleRes = R.string.assignment_detail_title
    const val ASSIGNMENT_ID_ARG = "assignmentId"
    val routeWithArgs = "$route/{$ASSIGNMENT_ID_ARG}"
}

//Manages layout of details screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentDetailsScreen(
    navigateToEditAssignment: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AssignmentDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        //Top title bar that reads "Assignment Details"
        topBar = {
            TopAppBar(
                title = stringResource(AssignmentDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        //Floating edit button in bottom right
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditAssignment(uiState.value.assignmentDetails.id) },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_assignment_title),
                )
            }
        },
        //Adds body where name, due date, and delete option are shown
        modifier = modifier,
    ) { innerPadding ->
        AssignmentDetailsBody(
            assignmentDetailsUiState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteAssignment()
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
        )
    }
}

//Creates and manages layout of details body
@Composable
private fun AssignmentDetailsBody(
    assignmentDetailsUiState: AssignmentDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        //Controls functions of delete button and delete confirmation dialog box
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        AssignmentDetails(
            assignment = assignmentDetailsUiState.assignmentDetails.toAssignment(), modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

//Creates assignment details card with name and due date
@Composable
fun AssignmentDetails(
    assignment: Assignment, modifier: Modifier = Modifier,
    @SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM, yyyy")
) {
    OutlinedCard(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            //Assignment Name
            AssignmentDetailsRow(
                labelResID = R.string.assignment,
                assignmentDetail = assignment.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            //Class Name
            AssignmentDetailsRow(
                labelResID = R.string.course,
                assignmentDetail = assignment.course,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            //Due Date
            AssignmentDetailsRow(
                labelResID = R.string.due_date,
                assignmentDetail = formatter.format((assignment.dueDate)).toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
        }

    }
}

//Holds details for display as individual lines in the card (
@Composable
private fun AssignmentDetailsRow(
    @StringRes labelResID: Int, assignmentDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = assignmentDetail, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
    }
}

//Dialog box for removing list entries when pressing "Complete assignment" button
@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}