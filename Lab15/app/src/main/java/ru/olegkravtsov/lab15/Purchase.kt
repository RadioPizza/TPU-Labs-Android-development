package ru.olegkravtsov.lab15

import java.io.Serializable

data class Purchase(
    val id: Long = System.currentTimeMillis(),
    var name: String,
    var quantity: String
) : Serializable