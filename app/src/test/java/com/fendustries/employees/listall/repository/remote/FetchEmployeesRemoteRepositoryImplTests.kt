package com.fendustries.employees.listall.repository.remote

import com.fendustries.employees.listall.network.response.NetworkResponse
import com.fendustries.employees.listall.repository.remote.responsemodel.Employees
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class FetchEmployeesRemoteRepositoryImplTests {
    @Test
    fun `given success with empty list, when get all employees, return success with empty list`() = runTest {
        // Given remote repo returns success with empty list
        val fetchEmployeesRemoteRepository = getRemoteRepo(Response.success(Employees(emptyList())))

        // When we get all employees
        val result = fetchEmployeesRemoteRepository.getAllEmployees()

        // Then return Success with empty list
        assertTrue(result is NetworkResponse.Success)
        val successResult = result as NetworkResponse.Success
        assertTrue(successResult.body.employees.isEmpty())
    }

    @Test
    fun `given success but with null body, when get all employees, return failure`() = runTest {
        // Given remote repo returns success but with null body
        val fetchEmployeesRemoteRepository = getRemoteRepo(Response.success(null))

        // When we get all employees
        val result = fetchEmployeesRemoteRepository.getAllEmployees()

        // Then return failure with exception
        assertTrue(result is NetworkResponse.Failure)
        val errorResult = result as NetworkResponse.Failure
        assertTrue(errorResult.throwable is IllegalStateException)
    }

    @Test
    fun `given api failure, when get all employees, return http failure`() = runTest {
        // Given remote repo returns api error
        val fetchEmployeesRemoteRepository = getRemoteRepo(Response.error(500, "".toResponseBody()))

        // When we get all employees
        val result = fetchEmployeesRemoteRepository.getAllEmployees()

        // Then return http failure with code
        assertTrue(result is NetworkResponse.HttpFailure)
        val errorResult = result as NetworkResponse.HttpFailure
        assertTrue(errorResult.statusCode == 500)
        assertTrue(errorResult.error == "")
    }

    @Test
    fun `given simulated serialization error, when get all employees, return failure`() = runTest {
        // Given remote repo throws exception due to serialization error
        val fetchEmployeesRemoteRepository = getRemoteRepo(null)

        // When we get all employees
        val result = fetchEmployeesRemoteRepository.getAllEmployees()

        // Then return failure with exception
        assertTrue(result is NetworkResponse.Failure)
        val errorResult = result as NetworkResponse.Failure
        assertTrue(errorResult.throwable is IllegalArgumentException)
    }

    private fun getRemoteRepo(response: Response<Employees>?): FetchEmployeesRemoteRepository {
        return FetchEmployeesRemoteRepositoryImpl(FakeFetchEmployeesAPIService(response))
    }
}