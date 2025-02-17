package com.fendustries.employees.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {
    @Serializable
    data object ListAllEmployees : Routes("List All Employees")
}