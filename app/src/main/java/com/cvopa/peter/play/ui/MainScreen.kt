package com.cvopa.peter.play.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cvopa.peter.play.MainScreenState
import com.cvopa.peter.play.MainViewModel
import com.cvopa.peter.play.ui.theme.ContactTheme
import com.cvopa.peter.fetchy.R
import com.cvopa.peter.play.Error
import com.cvopa.peter.play.Event
import com.cvopa.peter.play.usecase.LoginDetails
import timber.log.Timber

@Composable
fun MainScreen() {
    ContactTheme {
        val viewModel: MainViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        MainScreen(state) { viewModel.onEvent(it) }
    }
}

@Composable
private fun MainScreen(
    state: MainScreenState,
    onEvent: (Event) -> Unit = {}
) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Timber.d("peter state $state ")
            when (state) {
                is MainScreenState.Logout -> {
                    LoginScreen(
                        state = state,
                        onEvent = onEvent
                    )
                    ErrorScreen(error = state.error)
                }

                is MainScreenState.Login -> {
                    SuccessScreen(state = state, onEvent = onEvent)
                }
            }
        }
    }
}

@Composable
fun SuccessScreen(state: MainScreenState.Login, onEvent: (Event) -> Unit = {}) {
    Text(text = "Hurra $state")
    Image(bitmap = state.image.asImageBitmap(), contentDescription = null)
    Button(onClick = { onEvent(Event.OnUserNameReset) }) {
        Text("Logout")
    }

}

@Composable
fun ErrorScreen(error: Error?) {
    Text(text = "Error $error")
}

@Composable
fun LoginScreen(
    state: MainScreenState.Logout,
    onEvent: (Event) -> Unit,
) {

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            value = state.userName,
            onValueChange = { value ->
                onEvent(Event.OnUserNameChanged(value))
            },
            label = { Text(stringResource(id = R.string.user_name)) },
            trailingIcon = {
                if (state.userName.isNotEmpty()) {
                    Box(modifier = Modifier.clickable {
                        onEvent(Event.OnUserNameReset)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            value = state.userPassword,
            onValueChange = { value ->
                onEvent(Event.OnPasswordChanged(value))
            },
            label = {
                Text(stringResource(id = R.string.password))
            },
            trailingIcon = {
                if (state.userPassword.isNotEmpty()) {
                    Box(modifier = Modifier.clickable {
                        onEvent(Event.OnUserNameReset)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        Button(onClick = {
            onEvent(
                Event.OnLogin(
                    LoginDetails(
                        token = state.userPassword,
                        userName = state.userName,
                    )
                )
            )
        }) {
            Text(text = "Login")
        }
    }

}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(MainScreenState.Logout())
}