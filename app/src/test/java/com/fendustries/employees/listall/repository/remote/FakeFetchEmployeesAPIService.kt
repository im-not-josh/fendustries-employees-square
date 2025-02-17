package com.fendustries.employees.listall.repository.remote

import com.fendustries.employees.listall.network.FetchEmployeesAPIService
import com.fendustries.employees.listall.repository.remote.responsemodel.Employees
import retrofit2.Response

/**
 * Using a fake rather than a mock for unit tests.
 * I felt like it might be easier for folks to understand and maintain seeing an actual (fake) implementation
 * of this rather than a mock. This approach also means we dont have to do deal with final class mocking (Response is a final class)
 */
class FakeFetchEmployeesAPIService(private val response: Response<Employees>?) : FetchEmployeesAPIService {
    override suspend fun fetchAllEmployees(): Response<Employees> {
        return response ?: throw IllegalArgumentException()
    }
}