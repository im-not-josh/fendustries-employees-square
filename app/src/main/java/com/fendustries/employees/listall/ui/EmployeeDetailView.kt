package com.fendustries.employees.listall.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.fendustries.employees.R
import com.fendustries.employees.listall.vm.EmployeeDetailViewModel

@Composable
fun EmployeeDetailView(
    modifier: Modifier = Modifier,
    viewModel: EmployeeDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val employee = state.employee
    if (employee != null) {
        Column(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(employee.photo_url_large)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_person),
                error = painterResource(R.drawable.ic_person),
                contentDescription = stringResource(R.string.avatar_content_description),
                modifier = Modifier.fillMaxWidth()
            )

            Text(employee.full_name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(all = 8.dp))
            Text(employee.email_address, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(all = 8.dp))
            Text(employee.phone_number ?: "" , style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(all = 8.dp))
            Text(employee.team , style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(all = 8.dp))
            Text(employee.biography ?: "No bio", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(all = 8.dp))
        }
    }
}