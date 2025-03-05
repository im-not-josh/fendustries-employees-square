package com.fendustries.employees.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fendustries.employees.listall.ui.EmployeeDetailView
import com.fendustries.employees.listall.ui.ListAllUiContent

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.ListAllEmployees.route
    ) {
        composable(
            route = Routes.ListAllEmployees.route
        ) {
            ListAllUiContent(modifier, navController)
        }

        composable(
            route = Routes.EmployeeDetail.BASE_ROUTE_WITH_ARG,
            arguments = listOf(
                navArgument(Routes.EmployeeDetail.ARG) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        )
        {
            EmployeeDetailView(modifier)
        }

    }
}
