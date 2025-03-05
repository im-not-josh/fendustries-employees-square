package com.fendustries.employees.listall.vm

import com.fendustries.employees.navigation.Routes

sealed class ListAllEvents {
    data class NavigateToRoute(val routes: Routes) : ListAllEvents()
}