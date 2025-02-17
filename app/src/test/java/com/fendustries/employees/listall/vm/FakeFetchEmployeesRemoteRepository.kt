package com.fendustries.employees.listall.vm

import com.fendustries.employees.listall.network.response.NetworkResponse
import com.fendustries.employees.listall.repository.remote.FetchEmployeesRemoteRepository
import com.fendustries.employees.listall.repository.remote.responsemodel.Employees

/**
 * Using a fake rather than a mock for unit tests.
 * I felt like it might be easier for folks to understand and maintain seeing an actual (fake) implementation
 * of this rather than a mock.
 */
class FakeFetchEmployeesRemoteRepository(private val networkResponse: NetworkResponse<Employees>) : FetchEmployeesRemoteRepository {
    override suspend fun getAllEmployees(): NetworkResponse<Employees> {
        return networkResponse
    }
}