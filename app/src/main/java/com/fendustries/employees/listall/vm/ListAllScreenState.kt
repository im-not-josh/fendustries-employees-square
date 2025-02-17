package com.fendustries.employees.listall.vm

import androidx.annotation.StringRes
import com.fendustries.employees.R

/**
 * Describes the overall state of the List All Employees screen.
 * Using this to make rendering UI elements more clean and clear.
 * Our Error + No Employee views are also generic, so this helps our UI code to be simpler
 */
enum class ListAllScreenState(@StringRes val errorStringId: Int) {
    LOADING(R.string.loading),
    NETWORK_ERROR(R.string.network_error),
    OTHER_ERROR(R.string.general_error),
    NO_EMPLOYEES(R.string.no_results_error),
    SUCCESS(0)
}