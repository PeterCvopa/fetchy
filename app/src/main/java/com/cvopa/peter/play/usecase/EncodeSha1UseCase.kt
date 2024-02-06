package com.cvopa.peter.play.usecase

import com.cvopa.peter.play.util.UseCase
import com.google.common.hash.Hashing
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
class EncodeSha1UseCase @Inject constructor() : UseCase<String, String>() {
    override fun execute(input: String): String {
        return Hashing.sha1().hashString(input, Charsets.UTF_8).toString()
    }
}