package com.fendustries.employees.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {
    @Serializable
    data object ListAllEmployees : Routes("List All Employees")

    @Serializable
    data class EmployeeDetail(val key: String) : Routes(BASE_ROUTE_WITH_ARG) {
        companion object {
            const val ARG = "key"
            const val BASE_ROUTE = "EmployeeDetail"
            const val BASE_ROUTE_WITH_ARG = "$BASE_ROUTE/{$ARG}"
        }

        fun getRouteWithArgs(): String {
            return "$BASE_ROUTE/$key"
        }
    }
}