package com.cvopa.peter.play.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.cvopa.peter.play.util.UseCase
import javax.inject.Inject


class Decoder64BaseToBitmapUseCase @Inject constructor() : UseCase<String, Bitmap>() {
    override fun execute(input: String): Bitmap {
        return stringToBitMap(input) ?: throw IllegalStateException("Bitmap cannot be null")
    }

    private fun stringToBitMap(encodedString: String): Bitmap? {
        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}