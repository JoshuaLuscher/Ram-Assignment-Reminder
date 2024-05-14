package com.wcupa.assignmentNotifier.ui.assignment

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcupa.assignmentNotifier.R
import com.wcupa.assignmentNotifier.TopAppBar
import com.wcupa.assignmentNotifier.ui.AppViewModelProvider
import com.wcupa.assignmentNotifier.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

//Manages "Edit" screen for modifying an assignment after creation
object AssignmentEditDestination : NavigationDestination {
    override val route = "assignment_edit"
    override val titleRes = R.string.edit_assignment_title
    const val ASSIGNMENT_ID_ARG = "assignmentId"
    val routeWithArgs = "$route/{$ASSIGNMENT_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AssignmentEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    //Controls top bar that reads "Edit Assignment"
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(AssignmentEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
        //Edit body which updates entry based on what is entered into the text boxes
    ) { innerPadding ->
        AssignmentEntryBody(
            assignmentUiState = viewModel.assignmentUiState,
            onAssignmentValueChange = viewModel::updateUiState,
            //Save Button
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateAssignment()
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