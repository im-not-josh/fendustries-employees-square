package com.fendustries.employees.listall.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fendustries.employees.listall.repository.local.EmployeeLocal.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME
)
class EmployeeLocal(
    @PrimaryKey(
        autoGenerate = false
    )
    @ColumnInfo(
        name = COL_NAME_UUID
    )
    val uuid: String,
    val full_name: String,
    val phone_number: String?,
    val email_address: String,
    val biography: String?,
    val photo_url_small: String?,
    val photo_url_large: String?,
    val team: String,
) {
    companion object {
        const val TABLE_NAME = "EmployeeLocal"
        const val COL_NAME_UUID = "uuid"
    }
}