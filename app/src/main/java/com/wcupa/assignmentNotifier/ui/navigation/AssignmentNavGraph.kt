package com.wcupa.assignmentNotifier.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentDetailsDestination
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentDetailsScreen
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentEditDestination
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentEditScreen
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentEntryDestination
import com.wcupa.assignmentNotifier.ui.assignment.AssignmentEntryScreen
import com.wcupa.assignmentNotifier.ui.home.HomeDestination
import com.wcupa.assignmentNotifier.ui.home.HomeScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun AssignmentNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAssignmentEntry = { navController.navigate(AssignmentEntryDestination.route) },
                navigateToAssignmentUpdate = {
                    navController.navigate("${AssignmentDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = AssignmentEntryDestination.route) {
            AssignmentEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = AssignmentDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(AssignmentDetailsDestination.ASSIGNMENT_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            AssignmentDetailsScreen(
                navigateToEditAssignment = { navController.navigate("${AssignmentEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = AssignmentEditDestination.routeWithArgs,
            arguments = listOf(navArgument(AssignmentEditDestination.ASSIGNMENT_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            AssignmentEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
