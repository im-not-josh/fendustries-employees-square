package com.fendustries.employees.listall.vm

import app.cash.turbine.test
import com.fendustries.employees.listall.network.response.NetworkResponse
import com.fendustries.employees.listall.repository.remote.FetchEmployeesRemoteRepository
import com.fendustries.employees.listall.repository.remote.responsemodel.Employees
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ListAllViewModelTests {
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // This was needed to ensure that we are able to catch ALL uiState emissions.
        // Our uiState flow sometimes dropped changes, like LOADING state to SUCCESS if they
        // happened too quickly. This test dispatcher basically makes sure all state flow changes
        // are sent to the scheduler and are blocking until something else is sent, meaning we can
        // await and assert on each emission.
        // It is experimental, so theres likely a better way to do this.
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `given remote repo returns success with empty list, when vm created, then state is No Employees`() =
        runTest {
            // Given remote repo
            val remoteRepo = getRemoteRepo(NetworkResponse.Success(Employees(emptyList())))

            // When VM is created
            val viewModel = ListAllViewModel(remoteRepo)

            // Then ui state is no employees
            viewModel.uiState.test {
                val loadingState = awaitItem()
                assertEquals(ListAllScreenState.LOADING, loadingState.error)
                assertTrue(loadingState.allEmployees.isEmpty())

                val state = awaitItem()

                assertEquals(ListAllScreenState.NO_EMPLOYEES, state.error)
                assertTrue(state.allEmployees.isEmpty())
            }
        }

    @Test
    fun `given remote repo returns http failure, when vm created, then state is http failure`() =
        runTest {
            // Given remote repo
            val remoteRepo = getRemoteRepo(NetworkResponse.HttpFailure(501, ""))

            // When VM is created
            val viewModel = ListAllViewModel(remoteRepo)

            // Then ui state is network error
            viewModel.uiState.test {
                val loadingState = awaitItem()
                assertEquals(ListAllScreenState.LOADING, loadingState.error)
                assertTrue(loadingState.allEmployees.isEmpty())

                val state = awaitItem()

                assertEquals(ListAllScreenState.NETWORK_ERROR, state.error)
                assertTrue(state.allEmployees.isEmpty())
            }
        }

    @Test
    fun `given remote repo returns serialization failure, when vm created, then state is other failure`() =
        runTest {
            // Given remote repo
            val remoteRepo =
                getRemoteRepo(NetworkResponse.Failure(IllegalStateException("could not serialize json")))

            // When VM is created
            val viewModel = ListAllViewModel(remoteRepo)

            // Then ui state is other error
            viewModel.uiState.test {
                val loadingState = awaitItem()

                assertEquals(ListAllScreenState.LOADING, loadingState.error)
                assertTrue(loadingState.allEmployees.isEmpty())

                val state = awaitItem()

                assertEquals(ListAllScreenState.OTHER_ERROR, state.error)
                assertTrue(state.allEmployees.isEmpty())
            }
        }

    @Test
    fun `given remote repo returns success with list of employees, when vm created, then state is None`() =
        runTest {
            // Given remote repo
            val remoteRepo =
                getRemoteRepo(NetworkResponse.Success(Employees(getFakeEmployeeList())))

            // When VM is created
            val viewModel = ListAllViewModel(remoteRepo)

            // Then ui state is a list
            viewModel.uiState.test {
                val loadingState = awaitItem()
                assertEquals(ListAllScreenState.LOADING, loadingState.error)
                assertTrue(loadingState.allEmployees.isEmpty())

                val state = awaitItem()

                assertEquals(ListAllScreenState.SUCCESS, state.error)
                assertEquals(3, state.allEmployees.size)

                assertEquals("Adam A", state.allEmployees[0].name)
                assertEquals("Bruce B", state.allEmployees[1].name)
                assertEquals("Carli C", state.allEmployees[2].name)
            }
        }

    @Test
    fun `given remote repo returns success with list of employees, when refresh, then state is None`() =
        runTest {
            // Given remote repo
            val remoteRepo =
                getRemoteRepo(NetworkResponse.Success(Employees(getFakeEmployeeList())))
            val viewModel = ListAllViewModel(remoteRepo)

            // Then initial ui state is loading
            viewModel.uiState.test {
                launch {
                    // When refresh
                    viewModel.actions.trySend(ListAllActions.Refresh)
                }.join()

                val initialLoadingState = awaitItem()
                assertEquals(ListAllScreenState.LOADING, initialLoadingState.error)
                assertTrue(initialLoadingState.allEmployees.isEmpty())

                val initialLoadState = awaitItem()
                assertEquals(ListAllScreenState.SUCCESS, initialLoadState.error)
                assertEquals(3, initialLoadState.allEmployees.size)

                assertEquals("Adam A", initialLoadState.allEmployees[0].name)
                assertEquals("Bruce B", initialLoadState.allEmployees[1].name)
                assertEquals("Carli C", initialLoadState.allEmployees[2].name)

                val secondaryLoadingState = awaitItem()

                assertEquals(secondaryLoadingState.error, ListAllScreenState.LOADING)
                assertTrue(secondaryLoadingState.allEmployees.isEmpty())

                val refreshedFinalState = awaitItem()

                assertEquals(ListAllScreenState.SUCCESS, refreshedFinalState.error)
                assertEquals(3, refreshedFinalState.allEmployees.size)

                assertEquals("Adam A", refreshedFinalState.allEmployees[0].name)
                assertEquals("Bruce B", refreshedFinalState.allEmployees[1].name)
                assertEquals("Carli C", refreshedFinalState.allEmployees[2].name)
            }
        }

    private fun getRemoteRepo(networkResponse: NetworkResponse<Employees>): FetchEmployeesRemoteRepository {
        return FakeFetchEmployeesRemoteRepository(networkResponse)
    }
}