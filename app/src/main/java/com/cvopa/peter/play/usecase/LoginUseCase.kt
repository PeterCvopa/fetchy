package com.cvopa.peter.play.usecase

import android.graphics.Bitmap
import com.cvopa.peter.play.api.LoginService
import com.cvopa.peter.play.api.fromDomain
import com.cvopa.peter.play.util.SuspendedUseCase
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val loginService: LoginService,
    val decodeToBitmapUseCase: Decoder64BaseToBitmapUseCase
) : SuspendedUseCase<LoginDetails, SuccessLoginData>() {
    override suspend fun execute(input: LoginDetails): SuccessLoginData {
        val bitmap = decodeToBitmapUseCase(
            loginService.getImage(input.token, input.userName).fromDomain()
        )
        return SuccessLoginData(bitmap, input.userName)
    }
}

data class LoginDetails(val token: String, val userName: String)
data class SuccessLoginData(val bitmap: Bitmap, val userName: String)
