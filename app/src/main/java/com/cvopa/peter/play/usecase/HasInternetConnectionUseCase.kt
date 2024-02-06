package com.cvopa.peter.play.usecase

import com.cvopa.peter.play.util.NetworkMonitor
import com.cvopa.peter.play.util.UseCase
import javax.inject.Inject


class HasInternetConnectionUseCase @Inject constructor(val networkMonitor: NetworkMonitor) : UseCase<Unit, NetworkState>() {
    override fun execute(input: Unit): NetworkState {
        return if (networkMonitor.isNetworkAvailable()) NetworkState.Available else NetworkState.NotAvailable
    }
}

sealed class NetworkState {
    data object Available : NetworkState()
    data object NotAvailable : NetworkState()
}