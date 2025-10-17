package ru.olegkravtsov.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConversionViewModel : ViewModel() {
    private val _micrometers = MutableLiveData<String>()
    val micrometers: LiveData<String> get() = _micrometers

    private val _mils = MutableLiveData<String>()
    val mils: LiveData<String> get() = _mils

    private val _millimeters = MutableLiveData<String>()
    val millimeters: LiveData<String> get() = _millimeters

    private val _lines = MutableLiveData<String>()
    val lines: LiveData<String> get() = _lines

    private val _inches = MutableLiveData<String>()
    val inches: LiveData<String> get() = _inches

    private var isUpdating = false

    // Конвертация из микрометров
    fun updateMicrometers(value: String) {
        if (!isUpdating) {
            isUpdating = true
            _micrometers.value = value
            convertFromMicrometers(value)
            isUpdating = false
        }
    }

    private fun convertFromMicrometers(micrometerValue: String) {
        val micrometers = micrometerValue.toDoubleOrNull() ?: return

        val mils = micrometers / 25.4
        val millimeters = micrometers / 1000.0
        val lines = micrometers / 2540.0
        val inches = micrometers / 25400.0

        _mils.value = mils.toString()
        _millimeters.value = millimeters.toString()
        _lines.value = lines.toString()
        _inches.value = inches.toString()
    }

    // Конвертация из милов
    fun updateMils(value: String) {
        if (!isUpdating) {
            isUpdating = true
            _mils.value = value
            convertFromMils(value)
            isUpdating = false
        }
    }

    private fun convertFromMils(milValue: String) {
        val mils = milValue.toDoubleOrNull() ?: return

        val micrometers = mils * 25.4
        val millimeters = mils * 0.0254
        val lines = mils / 100.0
        val inches = mils / 1000.0

        _micrometers.value = micrometers.toString()
        _millimeters.value = millimeters.toString()
        _lines.value = lines.toString()
        _inches.value = inches.toString()
    }

    // Конвертация из миллиметров
    fun updateMillimeters(value: String) {
        if (!isUpdating) {
            isUpdating = true
            _millimeters.value = value
            convertFromMillimeters(value)
            isUpdating = false
        }
    }

    private fun convertFromMillimeters(millimeterValue: String) {
        val millimeters = millimeterValue.toDoubleOrNull() ?: return

        val micrometers = millimeters * 1000.0
        val mils = millimeters / 0.0254
        val lines = millimeters / 2.54
        val inches = millimeters / 25.4

        _micrometers.value = micrometers.toString()
        _mils.value = mils.toString()
        _lines.value = lines.toString()
        _inches.value = inches.toString()
    }

    // Конвертация из линий
    fun updateLines(value: String) {
        if (!isUpdating) {
            isUpdating = true
            _lines.value = value
            convertFromLines(value)
            isUpdating = false
        }
    }

    private fun convertFromLines(lineValue: String) {
        val lines = lineValue.toDoubleOrNull() ?: return

        val micrometers = lines * 2540.0
        val mils = lines * 100.0
        val millimeters = lines * 2.54
        val inches = lines / 10.0

        _micrometers.value = micrometers.toString()
        _mils.value = mils.toString()
        _millimeters.value = millimeters.toString()
        _inches.value = inches.toString()
    }

    // Конвертация из дюймов
    fun updateInches(value: String) {
        if (!isUpdating) {
            isUpdating = true
            _inches.value = value
            convertFromInches(value)
            isUpdating = false
        }
    }

    private fun convertFromInches(inchValue: String) {
        val inches = inchValue.toDoubleOrNull() ?: return

        val micrometers = inches * 25400.0
        val mils = inches * 1000.0
        val millimeters = inches * 25.4
        val lines = inches * 10.0

        _micrometers.value = micrometers.toString()
        _mils.value = mils.toString()
        _millimeters.value = millimeters.toString()
        _lines.value = lines.toString()
    }
}
