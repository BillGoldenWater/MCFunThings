package indi.goldenwater.mcfunthings.utils

import kotlin.math.abs

fun Double.toClosest(values: Array<Double>): Double {
    var lastDistance = Double.MAX_VALUE
    var result = 0.0

    values.forEach {
        val distance = abs(this - it)
        if (distance < lastDistance) {
            lastDistance = distance
            result = it
        }
    }

    return result
}