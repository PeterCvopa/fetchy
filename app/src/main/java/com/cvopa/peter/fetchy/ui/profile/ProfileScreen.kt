package com.cvopa.peter.fetchy.ui.profile

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cvopa.peter.fetchy.R
import com.cvopa.peter.fetchy.ui.components.ErrorMessage
import com.cvopa.peter.fetchy.ui.components.ErrorState
import com.cvopa.peter.fetchy.ui.components.LoadingIndicator
import com.cvopa.peter.fetchy.ui.theme.ContactTheme
import com.cvopa.peter.fetchy.usecase.LoginDetails
import com.cvopa.peter.fetchy.util.compose.isLandscape

@Composable
fun ProfileScreen() {
    ContactTheme {
        val viewModel: ProfileScreenViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        ProfileScreen(state) { viewModel.onEvent(it) }
    }
}

@Composable
private fun ProfileScreen(
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
                is MainScreenState.LoggedOut -> {
                    LoggedOutScreen(
                        state = state,
                        onEvent = onEvent
                    )
                }

                is MainScreenState.LoggedIn -> {
                    LoggedInScreen(state = state, onEvent = onEvent)
                }
            }
        }
    }
}

@Composable
private fun LoggedInScreen(state: MainScreenState.LoggedIn, onEvent: (Event) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val spacerHeight = if (isLandscape()) 12.dp else 20.dp
        Spacer(modifier = Modifier.height(spacerHeight))
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier
                    .weight(2f),
                elevation = cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(
                                BorderStroke(3.dp, Color.White),
                                CircleShape
                            ),
                        bitmap = state.image.asImageBitmap(), contentDescription = null
                    )
                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.user_name)
                        )
                        Text(text = state.userName)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(12.dp)
        ) {
            Button(onClick = { onEvent(Event.OnLogout) }) {
                Text(stringResource(id = R.string.logout))
            }
        }
    }
}


@Composable
private fun LoggedOutScreen(
    state: MainScreenState.LoggedOut,
    onEvent: (Event) -> Unit,
) {
    Column {
        UserNameField(state = state, onEvent = onEvent)
        PasswordField(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.weight(1f))

        ErrorMessage(error = state.error)
        LoadingIndicator(isLoading = state.isLoading)

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
private fun UserNameField(state: MainScreenState.LoggedOut, onEvent: (Event) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        value = state.userName,
        onValueChange = { value ->
            onEvent(Event.OnUserNameChanged(value))
        },
        singleLine = true,
        isError = state.error is ErrorState.InputErrorState,
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
private fun PasswordField(state: MainScreenState.LoggedOut, onEvent: (Event) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        value = state.userPassword,
        isError = state.error is ErrorState.InputErrorState,
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
                onEvent(Event.OnPasswordVisibilityToggle)
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
    ProfileScreen(MainScreenState.LoggedOut())
}


@Preview(apiLevel = 33, device = "id:Nexus 4")
@Composable
fun MainScreenPreviewError() {
    ProfileScreen(MainScreenState.LoggedOut(error = ErrorState.InputErrorState))
}


@Preview(apiLevel = 33, device = "id:Nexus 4")
@Composable
fun MainScreenPreviewLoading() {
    ProfileScreen(MainScreenState.LoggedOut(isLoading = true))
}

@Preview(apiLevel = 33, device = "id:Nexus 4")
@Composable
fun MainScreenPreviewLoggedIn() {
    val textBitmap = Bitmap.createBitmap(1024, 768, Bitmap.Config.ARGB_8888)
        .apply { eraseColor(android.graphics.Color.GREEN) }
    ProfileScreen(
        MainScreenState.LoggedIn(
            userName = "Peter The User",
            image = textBitmap
        )
    )
}

