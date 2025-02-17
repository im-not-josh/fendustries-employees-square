package com.fendustries.employees.listall.repository.remote

import com.fendustries.employees.listall.network.response.NetworkResponse
import com.fendustries.employees.listall.repository.remote.responsemodel.Employees

interface FetchEmployeesRemoteRepository {
    suspend fun getAllEmployees(): NetworkResponse<Employees>
}