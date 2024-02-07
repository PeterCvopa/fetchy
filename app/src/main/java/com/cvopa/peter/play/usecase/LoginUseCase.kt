package com.cvopa.peter.play.usecase

import android.graphics.Bitmap
import com.cvopa.peter.play.api.LoginResponse
import com.cvopa.peter.play.api.LoginService
import com.cvopa.peter.play.ui.base.SuspendedUseCase
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val loginService: LoginService,
    private val decodeToBitmapUseCase: Decoder64BaseToBitmapUseCase
) : SuspendedUseCase<LoginDetails, SuccessLoginData>() {
    override suspend fun execute(input: LoginDetails): SuccessLoginData {
        val bitmap = loginService.getImage(input.token, input.userName).fromDomain(decodeToBitmapUseCase)
        return SuccessLoginData(bitmap, input.userName)
    }
}

fun LoginResponse.fromDomain(decodeToBitmapUseCase: Decoder64BaseToBitmapUseCase): Bitmap = decodeToBitmapUseCase(this.image)

data class LoginDetails(val token: String, val userName: String)
data class SuccessLoginData(val bitmap: Bitmap, val userName: String)
