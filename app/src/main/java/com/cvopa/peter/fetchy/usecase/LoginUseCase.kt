package com.cvopa.peter.fetchy.usecase

import android.graphics.Bitmap
import com.cvopa.peter.fetchy.api.LoginService
import com.cvopa.peter.fetchy.ui.base.SuspendUseCase

import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginService: LoginService,
    private val decodeToBitmapUseCase: Decoder64BaseToBitmapUseCase
) : SuspendUseCase<LoginDetails, SuccessLoginData> {



    override suspend fun invoke(input: LoginDetails): SuccessLoginData {
        val response = loginService.getImage(input.token, input.userName)
        val bitmap = decodeToBitmapUseCase(response.image)
        return SuccessLoginData(bitmap, input.userName)
    }
}

data class LoginDetails(val token: String, val userName: String)
data class SuccessLoginData(val bitmap: Bitmap, val userName: String)
