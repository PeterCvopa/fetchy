package com.cvopa.peter.fetchy.usecase

import android.content.Context
import com.cvopa.peter.fetchy.ui.base.UseCase
import com.cvopa.peter.fetchy.util.context.hasInternet
import javax.inject.Inject


class
HasInternetConnectionUseCase @Inject constructor(private val context: Context) : UseCase<Unit, NetworkState> {
    override fun invoke(input: Unit): NetworkState {
        return if (context.hasInternet()) NetworkState.Available else NetworkState.NotAvailable
    }
}

sealed class NetworkState {
    data object Available : NetworkState()
    data object NotAvailable : NetworkState()
}