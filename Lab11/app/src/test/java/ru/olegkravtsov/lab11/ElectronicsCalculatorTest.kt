package ru.olegkravtsov.lab11

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ElectronicsCalculatorTest {

    private lateinit var calculator: ElectronicsCalculator

    @Before
    fun init() {
        calculator = ElectronicsCalculator()
    }

    @Test
    fun calculateVoltage_isCorrect() {
        // Обычные значения
        assertEquals(12.0, calculator.calculateVoltage(2.0, 6.0), 0.001)
        assertEquals(0.0, calculator.calculateVoltage(0.0, 10.0), 0.001)

        // Граничные значения
        assertEquals(1000000.0, calculator.calculateVoltage(1000.0, 1000.0), 0.001)
        assertEquals(0.001, calculator.calculateVoltage(0.001, 1.0), 0.001)
    }

    @Test
    fun calculatePower_isCorrect() {
        // Обычные значения
        assertEquals(24.0, calculator.calculatePower(2.0, 6.0), 0.001)
        assertEquals(0.0, calculator.calculatePower(0.0, 10.0), 0.001)

        // Граничные значения
        assertEquals(1.0E6, calculator.calculatePower(1000.0, 1.0), 0.001)
    }

    @Test
    fun calculateSeriesResistance_isCorrect() {
        // Обычные значения
        assertEquals(15.0, calculator.calculateSeriesResistance(5.0, 10.0), 0.001)
        assertEquals(30.0, calculator.calculateSeriesResistance(5.0, 10.0, 15.0), 0.001)

        // Граничные значения
        assertEquals(0.0, calculator.calculateSeriesResistance(), 0.001) // пустой список
        assertEquals(5.0, calculator.calculateSeriesResistance(5.0), 0.001) // один элемент
        assertEquals(0.0, calculator.calculateSeriesResistance(0.0, 0.0, 0.0), 0.001) // нулевые сопротивления
    }

    @Test
    fun calculateParallelResistance_isCorrect() {
        // Обычные значения
        assertEquals(3.333, calculator.calculateParallelResistance(5.0, 10.0), 0.001)
        assertEquals(2.0, calculator.calculateParallelResistance(6.0, 6.0, 6.0), 0.001)

        // Граничные значения
        assertEquals(0.0, calculator.calculateParallelResistance(), 0.001) // пустой список
        assertEquals(5.0, calculator.calculateParallelResistance(5.0), 0.001) // один элемент
        assertEquals(0.0, calculator.calculateParallelResistance(0.0, 10.0), 0.001) // одно сопротивление равно 0
    }

    @Test
    fun calculateInductiveReactance_isCorrect() {
        // Обычные значения
        assertEquals(62.831, calculator.calculateInductiveReactance(50.0, 0.2), 0.001)
        assertEquals(0.0, calculator.calculateInductiveReactance(0.0, 10.0), 0.001)
        assertEquals(0.0, calculator.calculateInductiveReactance(50.0, 0.0), 0.001)

        // Граничные значения (высокая частота)
        assertEquals(6283.185, calculator.calculateInductiveReactance(1000.0, 1.0), 0.001)
    }
}