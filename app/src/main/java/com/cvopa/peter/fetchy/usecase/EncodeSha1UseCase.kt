package com.cvopa.peter.fetchy.usecase

import com.cvopa.peter.fetchy.ui.base.UseCase
import com.google.common.hash.Hashing
import javax.inject.Inject

@Suppress("DEPRECATION")
class EncodeSha1UseCase @Inject constructor() : UseCase<String, String> {

    override fun invoke(input: String): String {
        return Hashing.sha1().hashString(input, Charsets.UTF_8).toString()
    }
}