package com.cvopa.peter.play.usecase

import android.content.Context
import com.cvopa.peter.play.ui.base.UseCase
import com.cvopa.peter.play.util.hasInternet
import javax.inject.Inject


class HasInternetConnectionUseCase @Inject constructor(private val context: Context) : UseCase<Unit, NetworkState>() {
    override fun execute(input: Unit): NetworkState {
        return if (context.hasInternet()) NetworkState.Available else NetworkState.NotAvailable
    }
}

sealed class NetworkState {
    data object Available : NetworkState()
    data object NotAvailable : NetworkState()
}