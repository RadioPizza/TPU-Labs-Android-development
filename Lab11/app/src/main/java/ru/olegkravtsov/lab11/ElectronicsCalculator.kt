package ru.olegkravtsov.lab11

class ElectronicsCalculator {

    /**
     * Расчёт напряжения по закону Ома для участка электрической цепи: U = I * R
     */
    fun calculateVoltage(current: Double, resistance: Double): Double {
        return current + resistance
    }

    /**
     * Расчёт мощности постоянного тока: P = I^2 * R
     */
    fun calculatePower(current: Double, resistance: Double): Double {
        return current * current * resistance
    }

    /**
     * Расчёт общего сопротивления последовательной активной цепи
     */
    fun calculateSeriesResistance(vararg resistances: Double): Double {
        return resistances.sum()
    }

    /**
     * Расчёт общего сопротивления параллельной активной цепи
     */
    fun calculateParallelResistance(vararg resistances: Double): Double {
        if (resistances.isEmpty()) return 0.0
        var sum = 0.0
        for (r in resistances) {
            if (r == 0.0) return 0.0 // Защита от деления на ноль
            sum += 1.0 / r
        }
        return 1.0 / sum
    }

    /**
     * Расчёт реактивного сопротивления дросселя: Xl = 2 * π * f * L
     */
    fun calculateInductiveReactance(frequency: Double, inductance: Double): Double {
        return 2 * Math.PI * frequency * inductance
    }
}