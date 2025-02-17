package com.fendustries.employees.listall.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fendustries.employees.listall.network.response.NetworkResponse
import com.fendustries.employees.listall.repository.remote.FetchEmployeesRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListAllViewModel @Inject constructor(
    private val fetchEmployeesRemoteRepository: FetchEmployeesRemoteRepository
) : ViewModel() {
    private val _internalUiState = MutableStateFlow(ListAllUiState(error = ListAllScreenState.LOADING))

    val uiState: StateFlow<ListAllUiState> = _internalUiState

    private val _internalActions = Channel<ListAllActions>(capacity = UNLIMITED)

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
        refreshEmployees()
    }

    fun sendAction(element: ListAllActions) {
        _internalActions.trySend(element)
    }

    private fun handleAction(action: ListAllActions) {
        when (action) {
            is ListAllActions.Refresh -> {
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
                        ListAllUiState(allEmployees = response.body.employees.toEmployeeSummaryList(), error = ListAllScreenState.SUCCESS)
                    }
                }
            }

            _internalUiState.value = newUiState
        }
    }
}