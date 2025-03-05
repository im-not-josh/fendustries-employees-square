package com.fendustries.employees.listall.vm

import com.fendustries.employees.listall.repository.remote.responsemodel.Employee
import com.fendustries.employees.listall.repository.remote.responsemodel.EmployeeType
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class EmployeeLocalSummaryTests {
    @Test
    fun `given a list of employees, when transform to employee summary, then order is alphabetical with correct info`() {
        // Given employee list
        val employeeList = getFakeEmployeeList()

        // When transform to employee summary list
        val employeeSummaryList = employeeList.toEmployeeSummaryList()

        // Then list is alphabetically ordered, with correct data
        assertEquals(3, employeeSummaryList.size)

        assertEquals("Adam A", employeeSummaryList[0].name)
        assertEquals("Team 1", employeeSummaryList[0].team)
        assertEquals("url-adam", employeeSummaryList[0].photoUrl)

        assertEquals("Bruce B", employeeSummaryList[1].name)
        assertEquals("Team 2", employeeSummaryList[1].team)
        assertEquals("url-bruce", employeeSummaryList[1].photoUrl)

        assertEquals("Carli C", employeeSummaryList[2].name)
        assertEquals("Team 3", employeeSummaryList[2].team)
        assertEquals("url-carli", employeeSummaryList[2].photoUrl)
    }

    @Test
    fun `given empty list of employees, when transform to employee summary, then empty list returned`() {
        // Given empty list
        val employeeList: List<Employee> = emptyList()

        // When transformed
        val employeeSummaryList = employeeList.toEmployeeSummaryList()

        // Then empty list returned
        assertTrue(employeeSummaryList.isEmpty())
    }
}