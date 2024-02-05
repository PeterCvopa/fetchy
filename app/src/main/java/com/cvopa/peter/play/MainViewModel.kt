package com.cvopa.peter.play

import android.graphics.Bitmap
import com.cvopa.peter.play.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<LoginScreenState>() {
    override val initialState: LoginScreenState
        get() = LoginScreenState.Login()


    fun test(){
        emitState(LoginScreenState.Error.NoInternet)
    }
}



sealed class LoginScreenState() {
    data class Login(
        val userName: String = "",
        val userPassword: String = "",
    ) : LoginScreenState()

    data class Success(val image: Bitmap) : LoginScreenState()
    sealed class Error(throwable: Throwable) : LoginScreenState() {
        data object NoInternet : Error(IllegalStateException())
        data class General(val throwable: Throwable) : Error(throwable)
    }
}
