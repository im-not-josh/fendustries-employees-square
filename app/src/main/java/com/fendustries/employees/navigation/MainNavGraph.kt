package com.fendustries.employees.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            ListAllUiContent(modifier)
        }
    }
}
