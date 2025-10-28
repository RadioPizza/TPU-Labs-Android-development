package ru.olegkravtsov.lab18

data class Currency(
    val name: String,
    val value: String,
    val nominal: Int,
    val charCode: String
) {
    // Форматированное название с номиналом
    fun getDisplayName(): String {
        return if (nominal == 1) {
            name
        } else {
            "$nominal $name"
        }
    }

    // Полное отображение с кодом валюты
    fun getFullDisplayName(): String {
        return if (nominal == 1) {
            "$name ($charCode)"
        } else {
            "$nominal $name ($charCode)"
        }
    }
}