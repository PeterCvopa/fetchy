package com.cvopa.peter.play.model

data class Contact(
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val isFavorite: Boolean,
)