package com.cvopa.peter.play.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {

    @FormUrlEncoded
    @POST("/download/bootcamp/image.php")
    suspend fun getImage(
        @Header("Authorization") token: String,
        @Field("username") userName: String,
    ): LoginResponse
}

data class LoginResponse(val image: String)

fun LoginResponse.fromDomain(): String = this.image
