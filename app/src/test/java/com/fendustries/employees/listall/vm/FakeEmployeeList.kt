package com.fendustries.employees.listall.vm

import com.fendustries.employees.listall.repository.remote.responsemodel.Employee
import com.fendustries.employees.listall.repository.remote.responsemodel.EmployeeType

fun getFakeEmployeeList() = listOf(
    Employee(
        uuid = "123",
        full_name = "Adam A",
        phone_number = "5555-55-55",
        email_address = "email@us.com",
        biography = "Hi",
        photo_url_small = "url-adam",
        photo_url_large = "url-large",
        team = "Team 1",
        employee_type = EmployeeType.FULL_TIME
    ),
    Employee(
        uuid = "789",
        full_name = "Carli C",
        phone_number = "5555-55-55",
        email_address = "email@us.com",
        biography = "Hi",
        photo_url_small = "url-carli",
        photo_url_large = "url-large",
        team = "Team 3",
        employee_type = EmployeeType.FULL_TIME
    ),
    Employee(
        uuid = "456",
        full_name = "Bruce B",
        phone_number = "5555-55-55",
        email_address = "email@us.com",
        biography = "Hi",
        photo_url_small = "url-bruce",
        photo_url_large = "url-large",
        team = "Team 2",
        employee_type = EmployeeType.FULL_TIME
    ),
)