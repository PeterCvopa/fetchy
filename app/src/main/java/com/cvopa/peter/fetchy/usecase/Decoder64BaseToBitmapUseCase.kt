package com.cvopa.peter.fetchy.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.cvopa.peter.fetchy.ui.base.UseCase
import javax.inject.Inject


class Decoder64BaseToBitmapUseCase @Inject constructor() : UseCase<String, Bitmap> {

    override fun invoke(input: String): Bitmap {
        return stringToBitMap(input) ?: throw IllegalStateException("Bitmap cannot be null")
    }

    private fun stringToBitMap(encodedString: String): Bitmap? {
        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}