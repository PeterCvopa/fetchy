package com.cvopa.peter.play

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.cvopa.peter.fetchy.R
import com.cvopa.peter.play.ui.common.BaseViewModel
import com.cvopa.peter.play.usecase.EncodeSha1UseCase
import com.cvopa.peter.play.usecase.HasInternetConnectionUseCase
import com.cvopa.peter.play.usecase.InputValidatorUseCase
import com.cvopa.peter.play.usecase.LoginDetails
import com.cvopa.peter.play.usecase.LoginUseCase
import com.cvopa.peter.play.usecase.NetworkState
import com.cvopa.peter.play.usecase.SuccessLoginData
import com.cvopa.peter.play.usecase.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val encodeSha1UseCase: EncodeSha1UseCase,
    val loginUseCase: LoginUseCase,
    val inputValidatorUseCase: InputValidatorUseCase,
    val hasInternetConnectionUseCase: HasInternetConnectionUseCase,
) : BaseViewModel<MainScreenState>() {

    override val initialState: MainScreenState
        get() = MainScreenState.Logout()

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnLogin -> onLogin(event.loginDetail)
            is Event.OnPasswordChanged -> onPasswordChanged(event.value)
            is Event.OnUserNameChanged -> onUserNameChanged(event.value)
            is Event.OnUserNameReset -> resetUserNameField()
            is Event.OnLogout -> onLogout()
            is Event.onPasswordVisibilirtyToggle -> onVisibilityPassToggle()
        }
    }

    private fun onVisibilityPassToggle() {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(isPasswordHidden = it.isPasswordHidden.not()))
            }
        }
    }

    private fun onLogout() {
        emitState(MainScreenState.Logout())
    }

    private fun onLogin(loginDetail: LoginDetails) {
        if (inputValidatorUseCase(loginDetail) == ValidationResult.Invalid) {
            setError(Error.InputError)
            return
        }

        if (hasInternetConnectionUseCase(Unit) == NetworkState.NotAvailable) {
            setError(Error.NoInternet)
            return
        }

        viewModelScope.launch {
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
                    if (it is HttpException && it.code() == 401) {
                        setError(Error.AuthorizationError)
                    } else {
                        setError(Error.General(it))
                    }
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

    private fun setSuccess(data: SuccessLoginData) {
        emitState(MainScreenState.Login(data.bitmap, data.userName))
    }

    private fun setError(error: Error) {
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
        resetError()
    }

    private fun onUserNameChanged(value: String) {
        state.value.let {
            if (it is MainScreenState.Logout) {
                emitState(it.copy(userName = value, userPassword = it.userPassword))
            }
        }
        resetError()
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
    data object onPasswordVisibilirtyToggle : Event()
    data object OnLogout : Event()
}

sealed class MainScreenState {
    data class Logout(
        val isPasswordHidden: Boolean = true,
        val isLoading: Boolean = false,
        val userName: String = "",
        val userPassword: String = "",
        val error: Error? = null
    ) : MainScreenState()

    data class Login(val image: Bitmap, val userName: String) : MainScreenState()
}

sealed class Error(open val throwable: Throwable, val messageRes: Int) {
    data object NoInternet : Error(IllegalStateException("No internet connection"), R.string.error_no_internet)
    data object InputError : Error(IllegalStateException("Input error "), R.string.error_input)
    data object AuthorizationError : Error(IllegalStateException("Wrong username or password"), R.string.authorization_input)
    data class General(override val throwable: Throwable) : Error(throwable, R.string.error_general)
}
