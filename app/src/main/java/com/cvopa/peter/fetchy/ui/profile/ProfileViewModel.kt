package com.cvopa.peter.fetchy.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.cvopa.peter.fetchy.ui.base.BaseViewModel
import com.cvopa.peter.fetchy.ui.components.ErrorState
import com.cvopa.peter.fetchy.usecase.EncodeSha1UseCase
import com.cvopa.peter.fetchy.usecase.HasInternetConnectionUseCase
import com.cvopa.peter.fetchy.usecase.InputValidatorUseCase
import com.cvopa.peter.fetchy.usecase.LoginDetails
import com.cvopa.peter.fetchy.usecase.LoginUseCase
import com.cvopa.peter.fetchy.usecase.NetworkState
import com.cvopa.peter.fetchy.usecase.SuccessLoginData
import com.cvopa.peter.fetchy.usecase.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    val encodeSha1UseCase: EncodeSha1UseCase,
    val loginUseCase: LoginUseCase,
    val inputValidatorUseCase: InputValidatorUseCase,
    val hasInternetConnectionUseCase: HasInternetConnectionUseCase,
) : BaseViewModel<MainScreenState>() {

    override val initialState: MainScreenState
        get() = MainScreenState.LoggedOut()

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnLogin -> onLogin(event.loginDetail)

            is Event.OnPasswordChanged -> {
                onPasswordChanged(event.value)
                resetError()
            }

            is Event.OnUserNameChanged -> {
                onUserNameChanged(event.value)
                resetError()
            }

            is Event.OnUserNameReset -> {
                resetUserNameField()
                resetError()
            }

            is Event.OnLogout -> onLogout()

            is Event.OnPasswordVisibilityToggle -> onVisibilityPassToggle()
        }
    }

    private fun onVisibilityPassToggle() {
        state.value.let {
            if (it is MainScreenState.LoggedOut) {
                emitState(it.copy(isPasswordHidden = it.isPasswordHidden.not()))
            }
        }
    }

    private fun onLogout() {
        emitState(MainScreenState.LoggedOut())
    }

    private fun onLogin(loginDetail: LoginDetails) {
        if (inputValidatorUseCase(loginDetail) == ValidationResult.Invalid) {
            setError(ErrorState.InputErrorState)
            return
        }

        if (hasInternetConnectionUseCase(Unit) == NetworkState.NotAvailable) {
            setError(ErrorState.NoInternet)
            return
        }

        viewModelScope.launch {
            runCatching {
                setLoading()
                loginUseCase(
                    loginDetail.copy(token = encodeSha1UseCase(input = loginDetail.token))
                )
            }
                .onFailure {
                    if (it is HttpException && it.code() == 401) {
                        setError(ErrorState.AuthorizationErrorState)
                    } else {
                        setError(ErrorState.General(it))
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
            if (it is MainScreenState.LoggedOut) {
                emitState(it.copy(isLoading = true))
            }
        }
    }

    private fun setSuccess(data: SuccessLoginData) {
        emitState(MainScreenState.LoggedIn(data.bitmap, data.userName))
    }

    private fun setError(error: ErrorState) {
        state.value.let {
            if (it is MainScreenState.LoggedOut) {
                emitState(it.copy(error = error, isLoading = false))
            }
        }
    }

    private fun resetError() {
        state.value.let {
            if (it is MainScreenState.LoggedOut) {
                emitState(it.copy(error = null, isLoading = false))
            }
        }
    }

    private fun onPasswordChanged(value: String) {
        state.value.let {
            if (it is MainScreenState.LoggedOut) {
                emitState(it.copy(userName = it.userName, userPassword = value))
            }
        }
    }

    private fun onUserNameChanged(value: String) {
        state.value.let {
            if (it is MainScreenState.LoggedOut) {
                emitState(it.copy(userName = value, userPassword = it.userPassword))
            }
        }
    }

    private fun resetUserNameField() {
        state.value.let {
            if (it is MainScreenState.LoggedOut) {
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
    data object OnPasswordVisibilityToggle : Event()
    data object OnLogout : Event()
}

sealed class MainScreenState {
    data class LoggedOut(
        val isPasswordHidden: Boolean = true,
        val isLoading: Boolean = false,
        val userName: String = "",
        val userPassword: String = "",
        val error: ErrorState? = null
    ) : MainScreenState()

    data class LoggedIn(val image: Bitmap, val userName: String) : MainScreenState()
}

