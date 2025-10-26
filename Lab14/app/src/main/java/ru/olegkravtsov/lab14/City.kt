package ru.olegkravtsov.lab14

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val title: String,
    val region: String,
    val district: String,
    val postalCode: String,
    val timezone: String,
    val population: String,
    val founded: String,
    val lat: Float,
    val lon: Float
) : Parcelable