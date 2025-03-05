package com.fendustries.employees.listall.vm

import com.fendustries.employees.listall.repository.local.EmployeeLocal
import com.fendustries.employees.listall.repository.remote.responsemodel.Employee

// A cut down version of Employee just for view purposes (we don't need or want to show all data from Employee)
data class EmployeeSummary(
    val name: String,
    val team: String,
    val photoUrl: String?,
)

fun List<Employee>.toEmployeeSummaryList1(): List<EmployeeSummary> {
    return this.map {
        EmployeeSummary(
            name = it.full_name,
            team = it.team,
            photoUrl = it.photo_url_small
        )
    }.sortedBy { it.name } // Alphabetical sort by default for now
}

fun List<Employee>.toEmployeeLocal(): List<EmployeeLocal> {
    return this.map {
        EmployeeLocal(
            it.uuid,
            it.full_name,
            it.phone_number,
            it.email_address,
            it.biography,
            it.photo_url_small,
            it.photo_url_large,
            team = it.team
        )
    }
}

fun List<EmployeeLocal>.toEmployeeSummaryList2(): List<EmployeeSummary> {
    return this.map {
        EmployeeSummary(
            it.full_name,
            it.team,
            it.photo_url_small
        )
    }.sortedBy { it.name } // Alphabetical sort by default for now
}