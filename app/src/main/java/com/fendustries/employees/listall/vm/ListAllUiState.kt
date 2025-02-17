package com.fendustries.employees.listall.vm

data class ListAllUiState(
    val allEmployees: List<EmployeeSummary> = emptyList(),
    val error: ListAllScreenState = ListAllScreenState.LOADING
)