package com.cvopa.peter.fetchy.viewmodel

import androidx.lifecycle.ViewModel
import com.cvopa.peter.fetchy.ui.components.ErrorState
import com.cvopa.peter.fetchy.ui.profile.MainScreenState
import com.cvopa.peter.fetchy.ui.profile.ProfileScreenViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

abstract class ViewModelStateVerifier<VM : ViewModel>(protected val viewModel: VM)

fun assertThat(viewModel: ProfileScreenViewModel): ProfileViewModelVerifier {
    return ProfileViewModelVerifier(viewModel)
}

class ProfileViewModelVerifier(viewModel: ProfileScreenViewModel) : ViewModelStateVerifier<ProfileScreenViewModel>(viewModel) {

    fun assertInitialState(): ProfileViewModelVerifier {
        assertEquals(viewModel.state.value, MainScreenState.LoggedOut())
        return this
    }

    fun assertPasswordIs(value: String): ProfileViewModelVerifier {
        assertEquals(viewModel.state.value, MainScreenState.LoggedOut(userPassword = value))
        return this
    }

    fun assertUserNameIs(value: String): ProfileViewModelVerifier {
        assertEquals(viewModel.state.value, MainScreenState.LoggedOut(userName = value))
        return this
    }

    fun assertIsNotLoading(): ProfileViewModelVerifier {
        val state = viewModel.state.value
        assertTrue(state !is MainScreenState.LoggedOut || state.isLoading.not())
        return this
    }

    fun assertNoError(): ProfileViewModelVerifier {
        val state = viewModel.state.value
        assertTrue(state is MainScreenState.LoggedOut && state.error == null || state is MainScreenState.LoggedIn)
        return this
    }

    fun assertError(errorState: ErrorState): ProfileViewModelVerifier {
        val state = viewModel.state.value
        assertTrue(state is MainScreenState.LoggedOut && state.error == errorState || state is MainScreenState.LoggedIn)
        return this
    }

    fun assertGeneralError(): ProfileViewModelVerifier {
        val state = viewModel.state.value
        assertTrue(state is MainScreenState.LoggedOut && state.error is ErrorState.General || state is MainScreenState.LoggedIn)
        return this
    }

    fun assertIsLogout(): ProfileViewModelVerifier {
        val state = viewModel.state.value
        assertTrue(state is MainScreenState.LoggedOut)
        return this
    }
}