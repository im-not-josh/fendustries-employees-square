package com.fendustries.employees.listall.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fendustries.employees.listall.network.response.NetworkResponse
import com.fendustries.employees.listall.repository.local.EmployeeDao
import com.fendustries.employees.listall.repository.remote.FetchEmployeesRemoteRepository
import com.fendustries.employees.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListAllViewModel @Inject constructor(
    private val fetchEmployeesRemoteRepository: FetchEmployeesRemoteRepository,
    private val employeeDao: EmployeeDao
) : ViewModel() {
    private val _internalUiState = MutableStateFlow(ListAllUiState(error = ListAllScreenState.LOADING))

    val uiState: StateFlow<ListAllUiState> = _internalUiState

    private val _internalActions = Channel<ListAllActions>(capacity = UNLIMITED)

    val actions: SendChannel<ListAllActions> = _internalActions

    private val _internalEvents = Channel<ListAllEvents>(capacity = UNLIMITED)

    val events: Flow<ListAllEvents> = _internalEvents.receiveAsFlow()

    init {
        _internalActions.consumeAsFlow()
            .onEach {
                handleAction(it)
            }
            .launchIn(viewModelScope)

        // We could trigger refreshing employees by using a screen load action instead.
        // For now, this seemed a better/simpler option.
        // Using the init block also means we wont refresh again for config changes like orientation
        // changes seeing as our VM instance survives those changes.
//        refreshEmployees()
        loadLocalEmployees()
    }

    private fun handleAction(action: ListAllActions) {
        when (action) {
            is ListAllActions.Refresh -> {
                refreshEmployees()
            }
            is ListAllActions.TapEmployee -> {
                _internalEvents.trySend(ListAllEvents.NavigateToRoute(Routes.EmployeeDetail(action.key)))
            }
        }
    }

    private fun loadLocalEmployees() {
        val loadingState = ListAllUiState(emptyList(), ListAllScreenState.LOADING)
        _internalUiState.value = loadingState

        viewModelScope.launch {
            val result = employeeDao.getAllEmployees()

            val newUiState = if (result.isEmpty()) {
                ListAllUiState(allEmployees = emptyList(), error = ListAllScreenState.NO_EMPLOYEES)

            } else {
                ListAllUiState(allEmployees = result.toEmployeeSummaryList2(), error = ListAllScreenState.SUCCESS)
            }

            _internalUiState.value = newUiState

            if (result.isEmpty()) {
                refreshEmployees()
            }
        }
    }

    private fun refreshEmployees() {
        val loadingState = ListAllUiState(emptyList(), ListAllScreenState.LOADING)
        _internalUiState.value = loadingState

        viewModelScope.launch {
            val response = fetchEmployeesRemoteRepository.getAllEmployees()

            // Based on the NetworkResponse, determine our UI + Screen State.
            val newUiState = when (response) {
                is NetworkResponse.Failure -> {
                    ListAllUiState(allEmployees = emptyList(), error = ListAllScreenState.OTHER_ERROR)
                }
                is NetworkResponse.HttpFailure -> {
                    ListAllUiState(allEmployees = emptyList(), error = ListAllScreenState.NETWORK_ERROR)
                }
                is NetworkResponse.Success -> {
                    if (response.body.employees.isEmpty()) {
                        ListAllUiState(allEmployees = emptyList(), error = ListAllScreenState.NO_EMPLOYEES)
                    } else {
                        employeeDao.deleteAll()
                        employeeDao.insertAll(response.body.employees.toEmployeeLocal())

                        ListAllUiState(allEmployees = response.body.employees.toEmployeeSummaryList1(), error = ListAllScreenState.SUCCESS)
                    }
                }
            }

            _internalUiState.value = newUiState
        }
    }
}