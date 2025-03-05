package com.fendustries.employees.listall.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fendustries.employees.listall.repository.local.EmployeeDao
import com.fendustries.employees.listall.repository.remote.FetchEmployeesRemoteRepository
import com.fendustries.employees.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeDetailViewModel @Inject constructor(
    private val employeeDao: EmployeeDao,
    stateHandle: SavedStateHandle
) : ViewModel() {
    private val key: String = stateHandle.get(Routes.EmployeeDetail.ARG) ?: ""

    private val _internalUiState = MutableStateFlow(EmployeeDetailUiState())

    val uiState: StateFlow<EmployeeDetailUiState> = _internalUiState

    init {
        viewModelScope.launch {
            val newUiState = EmployeeDetailUiState(employeeDao.getEmployeeByKey(key))

            _internalUiState.value = newUiState
        }
    }
}