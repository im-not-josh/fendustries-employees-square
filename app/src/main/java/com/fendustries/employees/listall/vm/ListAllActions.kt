package com.fendustries.employees.listall.vm

/**
 * User triggered or lifecycle triggered actions our VM should respond to
 */
sealed class ListAllActions {
    data object Refresh : ListAllActions()
}