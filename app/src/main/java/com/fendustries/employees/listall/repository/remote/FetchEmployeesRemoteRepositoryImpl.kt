package com.fendustries.employees.listall.repository.remote

import com.fendustries.employees.listall.network.FetchEmployeesAPIService
import com.fendustries.employees.listall.network.response.NetworkResponse
import com.fendustries.employees.listall.repository.remote.responsemodel.Employees
import javax.inject.Inject

/**
 * This class makes API calls to remote servers and parses the results.
 * Return types should be in format NetworkResponse<GenericType> so consumers can
 * understand errors easily.
 */
class FetchEmployeesRemoteRepositoryImpl @Inject constructor(
    private val fetchEmployeesAPIService: FetchEmployeesAPIService
) : FetchEmployeesRemoteRepository {
    override suspend fun getAllEmployees(): NetworkResponse<Employees> {
        // Try Catch block because malformed json responses with KotlinX serialization actually throw exceptions
        return try {
            val response = fetchEmployeesAPIService.fetchAllEmployees()

            // isSuccessful looks for a 2XX http response, example 200 or 201
            return if (response.isSuccessful) {
                val body = response.body()

                if (body == null) {
                    // Possible but unlikely for body to be empty - we will treat as error because we always expect _something_ here
                    NetworkResponse.Failure(IllegalStateException("Body was null"))
                } else {
                    NetworkResponse.Success(body)
                }
            } else {
                NetworkResponse.HttpFailure(response.code(), response.errorBody()?.string())
            }
        } catch (ex: Exception) {
            NetworkResponse.Failure(ex)
        }
    }
}