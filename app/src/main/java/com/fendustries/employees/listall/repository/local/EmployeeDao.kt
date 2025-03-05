package com.fendustries.employees.listall.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fendustries.employees.listall.repository.local.EmployeeLocal.Companion.TABLE_NAME

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllEmployees(): List<EmployeeLocal>

    @Insert
    suspend fun insertAll(employees: List<EmployeeLocal>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}