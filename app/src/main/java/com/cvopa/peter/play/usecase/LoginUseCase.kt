package com.cvopa.peter.play.usecase

import android.graphics.Bitmap
import com.cvopa.peter.play.api.LoginService
import com.cvopa.peter.play.api.fromDomain
import com.cvopa.peter.play.util.SuspendedUseCase
import javax.inject.Inject
import javax.inject.Singleton


class LoginUseCase @Inject constructor(
    private val loginService: LoginService,
    val decodeToBitmapUseCase: Decoder64BaseToBitmapUseCase
) : SuspendedUseCase<LoginDetails, Bitmap>() {
    override suspend fun execute(input: LoginDetails): Bitmap {
        return decodeToBitmapUseCase(
            loginService.getImage(input.token, input.userName).fromDomain()
        )
    }
}


data class LoginDetails(val token: String, val userName: String)
