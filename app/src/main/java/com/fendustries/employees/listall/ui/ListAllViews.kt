package com.fendustries.employees.listall.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.fendustries.employees.R
import com.fendustries.employees.listall.vm.EmployeeSummary
import com.fendustries.employees.listall.vm.ListAllActions
import com.fendustries.employees.listall.vm.ListAllEvents
import com.fendustries.employees.listall.vm.ListAllScreenState
import com.fendustries.employees.listall.vm.ListAllViewModel
import com.fendustries.employees.navigation.Routes
import com.fendustries.employees.ui.theme.FendustriesEmployeesTheme
import kotlinx.coroutines.flow.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAllUiContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ListAllViewModel = hiltViewModel())
{
    val localConfig = LocalConfiguration.current

    LaunchedEffect(Unit) {
        viewModel.events.collect() {
            when (it) {
                is ListAllEvents.NavigateToRoute -> {
                    when (it.routes) {
                        is Routes.EmployeeDetail -> {
                            navController.navigate(it.routes.getRouteWithArgs())
                        }
                        else -> {
                            // Do nothing
                        }
                    }
                }
            }
        }
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            actions = {
                // Refresh button only makes sense if the screen state is success.
                // All other screen states have a Trg Again button in line instead
                if (state.error == ListAllScreenState.SUCCESS) {
                    // Its amazing how simple Jetpack compose has made adding a toolbar action - 3 lines of code to refresh, very cool
                    IconButton(onClick = {
                        viewModel.actions.trySend(ListAllActions.Refresh)
                    }) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Refresh")
                    }
                }
            }
        )
    }) { padding ->
        if (state.error != ListAllScreenState.SUCCESS) {
            ListAllErrorUiContent(
                state.error,
                modifier.padding(padding),
                { viewModel.actions.trySend(ListAllActions.Refresh) }
            )
        } else {
            // In landscape orientation, lazy col looks terrible, so switching to lazy row.
            // Our EmployeeSummaryCard doesn't look the best it could be in landscape, but better
            // than lazy col
            if (localConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(state.allEmployees) { item ->
                        EmployeeSummaryCard(item) {
                            viewModel.actions.trySend(ListAllActions.TapEmployee(it))
                        }
                    }
                }
            } else {
                LazyRow(modifier = Modifier.padding(padding)) {
                    items(state.allEmployees) { item ->
                        EmployeeSummaryCard(item) {
                            viewModel.actions.trySend(ListAllActions.TapEmployee(it))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeSummaryCard(
    employeeSummary: EmployeeSummary,
    onTap: (String) -> Unit
) {
    Card(modifier = Modifier.padding(all = 8.dp).fillMaxWidth().selectable(false) {
        onTap.invoke(employeeSummary.key)
    }) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(employeeSummary.photoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_person),
                error = painterResource(R.drawable.ic_person),
                contentDescription = stringResource(R.string.avatar_content_description),
                modifier = Modifier.size(60.dp).clip(CircleShape).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                Text(employeeSummary.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(employeeSummary.team, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeSummaryCardPreview() {
    EmployeeSummaryCard(
        EmployeeSummary("", "Joe Bloggs", "Awesome Team", "somUrl")
    ){
        // Do nothing for now
    }
}

@Composable
fun ListAllErrorUiContent(
    error: ListAllScreenState,
    modifier: Modifier,
    onTryAgainClicked: () -> Unit
) {
    // This check makes sure we are only rendering views when the screen state is not success.
    // In success states we want to render the list of employees instead.
    // An alternative could be to throw an IllegalArgument exception here instead - at least then a mistake
    // is easily spotted instead of seeing a blank screen, but runs the risk of exceptions in prod.
    if (error != ListAllScreenState.SUCCESS) {
        Column(
            modifier = modifier.fillMaxSize().padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(
                    error.errorStringId
                ),
                Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )

            when (error) {
                ListAllScreenState.OTHER_ERROR,
                ListAllScreenState.NO_EMPLOYEES,
                ListAllScreenState.NETWORK_ERROR -> {
                    Button(onClick = onTryAgainClicked) {
                        Text(stringResource(R.string.try_again_button_text))
                    }
                }
                else -> {
                    // Do nothing, handled by other code paths outside this func
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListAllErrorUiContentLoadingPreview() {
    FendustriesEmployeesTheme {
        ListAllErrorUiContent(
            ListAllScreenState.LOADING,
            Modifier,
            { } // Intentionally empty onTryAgainClick func
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListAllErrorUiContentNoEmployeesPreview() {
    FendustriesEmployeesTheme {
        ListAllErrorUiContent(
            ListAllScreenState.NO_EMPLOYEES,
            Modifier,
            { } // Intentionally empty onTryAgainClick func
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ListAllErrorUiContentOtherPreview() {
    FendustriesEmployeesTheme {
        ListAllErrorUiContent(
            ListAllScreenState.OTHER_ERROR,
            Modifier,
            { } // Intentionally empty onTryAgainClick func
        )
    }
}