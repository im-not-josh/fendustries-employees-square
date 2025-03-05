package com.fendustries.employees.listall.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = false,
    entities = [EmployeeLocal::class]
)
abstract class EmployessDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}