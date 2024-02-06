package com.cvopa.peter.play

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.cvopa.peter.play.ui.common.BaseViewModel
import com.cvopa.peter.play.usecase.EncodeSha1UseCase
import com.cvopa.peter.play.usecase.LoginDetails
import com.cvopa.peter.play.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val encodeSha1UseCase: EncodeSha1UseCase,
    val loginUseCase: LoginUseCase,
) : BaseViewModel<MainScreenState>() {

    override val initialState: MainScreenState
        get() = MainScreenState.Logout()

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnLogin -> onLogin(event.loginDetail)
            is Event.OnPasswordChanged -> onPasswordChanged(event.value)
            is Event.OnPasswordReset -> resetPasswordField()
            is Event.OnUserNameChanged -> onUserNameChanged(event.value)
            is Event.OnUserNameReset -> resetUserNameField()
            is Event.OnLogout -> onLogout()
        }
    }

    private fun onLogout() {
        emitState(MainScreenState.Logout())
    }

    private fun onLogin(loginDetail: LoginDetails) {
        viewModelScope.launch {

            //min delay add here napr zip ?
            runCatching {
                setLoading()
                loginUseCase(
                    LoginDetails(
                        encodeSha1UseCase(loginDetail.token),
                        loginDetail.userName
                    )
                )
            }
                .onFailure {
                    setError(Error.General(it))
                    Timber.e(it)
                }
                .onSuccess {
                    setSuccess(it)
                }

        }
    }

    private fun setLoading() {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(isLoading = true))
            }
        }
    }

    private fun setSuccess(bitmap: Bitmap) {
        emitState(MainScreenState.Login(bitmap))
    }

    private fun setError(error: Error) {
        Timber.d("peter $error")
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(error = error, isLoading = false))
            }
        }
    }

    private fun resetError() {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(error = null, isLoading = false))
            }
        }
    }


    private fun onPasswordChanged(value: String) {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(userName = it.userName, userPassword = value))
            }
        }
    }

    private fun onUserNameChanged(value: String) {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(userName = value, userPassword = it.userPassword))
            }
        }

    }

    private fun resetPasswordField() {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(userName = it.userName, userPassword = ""))
            }
        }
    }

    private fun resetUserNameField() {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(userName = "", userPassword = it.userPassword))
            }
        }
    }

}

sealed class Event {
    class OnLogin(val loginDetail: LoginDetails) : Event()
    class OnUserNameChanged(val value: String) : Event()
    class OnPasswordChanged(val value: String) : Event()
    data object OnUserNameReset : Event()
    data object OnPasswordReset : Event()
    data object OnLogout : Event()
}


sealed class MainScreenState {
    data class Logout(
        val isLoading: Boolean = false,
        val userName: String = "",
        val userPassword: String = "",
        val error: Error? = null
    ) : MainScreenState()

    data class Login(val image: Bitmap) : MainScreenState()
}

sealed class Error(throwable: Throwable) {
    data object NoInternet : Error(IllegalStateException())
    data class General(val throwable: Throwable) : Error(throwable)
}
