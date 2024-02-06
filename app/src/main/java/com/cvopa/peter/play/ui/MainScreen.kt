package com.cvopa.peter.play.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
        Column(
            modifier = Modifier
                .padding(it)
                .then(Modifier.padding(top = 30.dp))
        ) {
            when (state) {
                is MainScreenState.Logout -> {
                    LoginScreen(
                        state = state,
                        onEvent = onEvent
                    )
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .padding(horizontal = 32.dp),
            bitmap = state.image.asImageBitmap(), contentDescription = null
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Row {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = stringResource(id = R.string.user_name)
                )
            }
            Row {
                Text(text = state.userName)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(12.dp)
        ) {
            Button(onClick = { onEvent(Event.OnUserNameReset) }) {
                Text(stringResource(id = R.string.logout))
            }
        }
    }
}

@Composable
fun ErrorScreen(error: Error?) {
    if (error == null) return
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            Icon(
                tint = Color.Red,
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = null
            )
        }

        Row {
            Text(
                maxLines = 2,
                color = Color.Red,
                text = stringResource(id = error.messageRes)
            )
        }

    }
}

@Composable
fun LoginScreen(
    state: MainScreenState.Logout,
    onEvent: (Event) -> Unit,
) {
    Column {
        UserNameField(state = state, onEvent = onEvent)
        PasswordField(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.weight(1f))

        ErrorScreen(error = state.error)
        LoadingIndicator(state = state)
        Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            Button(
                modifier = Modifier.padding(vertical = 20.dp),
                onClick = {
                    onEvent(
                        Event.OnLogin(
                            LoginDetails(
                                token = state.userPassword,
                                userName = state.userName,
                            )
                        )
                    )
                }) {
                Text(
                    text = stringResource(id = R.string.login).uppercase()
                )
            }
        }
    }
}

@Composable
private fun LoadingIndicator(state: MainScreenState.Logout) {
    if (state.isLoading) {
        Row(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }
    }
}

@Composable
private fun UserNameField(state: MainScreenState.Logout, onEvent: (Event) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        value = state.userName,
        onValueChange = { value ->
            onEvent(Event.OnUserNameChanged(value))
        },
        singleLine = true,
        isError = state.error is Error.InputError,
        label = {
            Text(stringResource(id = R.string.user_name))
        },
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
}

@Composable
private fun PasswordField(state: MainScreenState.Logout, onEvent: (Event) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        value = state.userPassword,
        isError = state.error is Error.InputError,
        singleLine = true,
        onValueChange = { value ->
            onEvent(Event.OnPasswordChanged(value))
        },
        label = {
            Text(stringResource(id = R.string.password))
        },
        visualTransformation = if (state.isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = {
                onEvent(Event.onPasswordVisibilirtyToggle)
            }) {
                val visibilityIcon =
                    if (state.isPasswordHidden) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                Icon(painter = painterResource(id = visibilityIcon), contentDescription = null)
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                onEvent(
                    Event.OnLogin(
                        LoginDetails(
                            token = state.userPassword,
                            userName = state.userName,
                        )
                    )
                )
            }
        )
    )
}


@Preview(apiLevel = 33, device = "id:Nexus 4")
@Composable
fun MainScreenPreview() {
    MainScreen(MainScreenState.Logout())
}


@Preview(apiLevel = 33, device = "id:Nexus 4")
@Composable
fun MainScreenPreviewError() {
    MainScreen(MainScreenState.Logout(error = Error.InputError))
}


@Preview(apiLevel = 33, device = "id:Nexus 4")
@Composable
fun MainScreenPreviewLoading() {
    MainScreen(MainScreenState.Logout(isLoading = true))
}

@Preview(apiLevel = 33, device = "id:Nexus 4")
@Composable
fun MainScreenPreviewLoFFF() {
    val context = LocalContext.current
    MainScreen(
        MainScreenState.Login(
            userName = "Peter User",
            image = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background)
        )
    )
}

