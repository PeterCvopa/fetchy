package com.cvopa.peter.play.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cvopa.peter.play.LoginScreenState
import com.cvopa.peter.play.MainViewModel
import com.cvopa.peter.play.ui.theme.ContactTheme

@Composable
fun MainScreen() {
    ContactTheme {
        val viewModel: MainViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        MainScreen(state) { viewModel.test() }
    }
}

@Composable
private fun MainScreen(state: LoginScreenState, onClick: () -> Unit = {}) {
    val test = when (state) {
        is LoginScreenState.Error.General -> "error G"
        LoginScreenState.Error.NoInternet -> "No internner"
        is LoginScreenState.Login -> "login"
        is LoginScreenState.Success -> "success"
    }

    Scaffold() {
        Column {
            Row(modifier = Modifier.padding(it)) {
                Text(text = test)
            }

            Button(onClick = onClick) {
                Text(text = "click me")
            }
        }
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(LoginScreenState.Login())
}