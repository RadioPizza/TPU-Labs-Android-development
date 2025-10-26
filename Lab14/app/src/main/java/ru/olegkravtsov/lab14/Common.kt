package ru.olegkravtsov.lab14

import android.content.Context
import android.util.Log

object Common {
    val cities = mutableListOf<City>()
    var selectedCity: City? = null

    fun initCities(ctx: Context) {
        if (cities.isEmpty()) {
            try {
                val inputStream = ctx.resources.openRawResource(R.raw.cities)
                val lines = inputStream.bufferedReader().readLines()

                for (i in 1 until lines.size) {
                    val line = lines[i].trim()
                    if (line.isNotEmpty()) {
                        val parts = line.split(";")
                        if (parts.size >= 9) {
                            try {
                                val city = City(
                                    title = parts[3].trim(),
                                    region = parts[2].trim(),
                                    district = parts[1].trim(),
                                    postalCode = parts[0].trim(),
                                    timezone = parts[4].trim(),
                                    population = parts[7].trim(),
                                    founded = parts[8].trim(),
                                    lat = parts[5].trim().toFloat(),
                                    lon = parts[6].trim().toFloat()
                                )
                                cities.add(city)
                            } catch (e: NumberFormatException) {
                                Log.e("Common", "Error parsing numeric data", e)
                            } catch (e: Exception) {
                                Log.e("Common", "Error parsing data", e)
                            }
                        }
                    }
                }
                cities.sortBy { it.title }
                Log.d("Common", "Loaded ${cities.size} cities")
            } catch (e: Exception) {
                Log.e("Common", "Error loading cities", e)
            }
        }
    }
}