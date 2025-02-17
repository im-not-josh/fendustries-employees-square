package com.fendustries.employees.listall.network

import com.fendustries.employees.listall.repository.remote.responsemodel.Employees
import retrofit2.Response
import retrofit2.http.GET

interface FetchEmployeesAPIService {
    @GET("employees.json")
//    @GET("employees_empty.json") // Uncomment this line, comment other GET lines to test empty scenario
//    @GET("employees_malformed.json") // Uncomment this line, comment other GET lines to test malformed json scenario
    suspend fun fetchAllEmployees(): Response<Employees>
}