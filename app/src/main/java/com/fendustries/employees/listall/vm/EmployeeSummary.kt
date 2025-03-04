package com.fendustries.employees.listall.vm

import com.fendustries.employees.listall.repository.remote.responsemodel.Employee

// A cut down version of Employee just for view purposes (we don't need or want to show all data from Employee)
data class EmployeeSummary(
    val name: String,
    val team: String,
    val photoUrl: String?,
)

fun List<Employee>.toEmployeeSummaryList(): List<EmployeeSummary> {
    return this.map {
        EmployeeSummary(
            name = it.full_name,
            team = it.team,
            photoUrl = it.photo_url_small
        )
    }.sortedBy { it.name } // Alphabetical sort by default for now
}