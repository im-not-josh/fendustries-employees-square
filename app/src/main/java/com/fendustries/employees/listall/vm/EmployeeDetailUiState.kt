package com.fendustries.employees.listall.vm

import com.fendustries.employees.listall.repository.local.EmployeeLocal

data class EmployeeDetailUiState(
    val employee: EmployeeLocal? = null
)