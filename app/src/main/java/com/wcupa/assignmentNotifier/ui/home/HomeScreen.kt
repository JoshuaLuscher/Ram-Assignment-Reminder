package com.wcupa.assignmentNotifier.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcupa.assignmentNotifier.R
import com.wcupa.assignmentNotifier.TopAppBar
import com.wcupa.assignmentNotifier.data.Assignment
import com.wcupa.assignmentNotifier.ui.AppViewModelProvider
import com.wcupa.assignmentNotifier.ui.navigation.NavigationDestination
import java.text.SimpleDateFormat


//HomeDestination object used for AssignmentNavGraph
object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

//Manages layout of the home screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAssignmentEntry: () -> Unit,
    navigateToAssignmentUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //Top title bar that reads "Upcoming Assignments:"
        topBar = {
            TopAppBar(
                title = stringResource(R.string.header),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },

        //Floating Action button for creating list entries
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAssignmentEntry,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = stringResource(R.string.assignment_entry_title)
                )
            }
        },

        //Displays the list of assignments
    ) { innerPadding ->
        HomeBody(
            assignmentList = homeUiState.assignmentList,
            onAssignmentClick = navigateToAssignmentUpdate,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

//Controls how the list of multiple entries are displayed
@Composable
private fun HomeBody(
    assignmentList: List<Assignment>,
    onAssignmentClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        //If the list is empty, display no_assignment_description String
        if (assignmentList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_assignment_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        //Else, display the List
        } else {
            AssignmentList(
                assignmentList = assignmentList,
                onAssignmentClick = { onAssignmentClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

//Function used to create and and display the List of Assignments
@Composable
private fun AssignmentList(
    assignmentList: List<Assignment>,
    onAssignmentClick: (Assignment) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {

        items(items = assignmentList, key = { it.id }) { item ->
            AssignmentItem(assignment = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onAssignmentClick(item) })
        }
    }
}

//Controls how cards (Individual list items) are displayed
@Composable
private fun AssignmentItem(
    assignment: Assignment, modifier: Modifier = Modifier,
    @SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM")
) {
    OutlinedCard(
        modifier = modifier
            .padding(1.dp)
            .fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row{

                //Assignment name/course column
                Column(
                    modifier = Modifier.weight(2f, fill = false),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Assignment Name Text
                    Row {
                        Text(
                            text = "◆ ",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = assignment.name,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(Modifier.padding(3.dp))
                    //Course text
                    Row {
                        //Use for spacing to align text with just the assignment name without the "◆"
                        Text(
                            text = "    ",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                        )
                        //Displays the Text itself
                        Text(
                            text = assignment.course,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }

                }
                Spacer(modifier = Modifier.weight(0.2f))
                //Due Date display
                Column(
                    Modifier.weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Due Date:",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center

                    )
                    Text(
                        text = formatter.format((assignment.dueDate)).toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center

                    )
                }
            }
        }
    }
}

