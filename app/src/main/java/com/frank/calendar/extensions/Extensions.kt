package com.frank.calendar.extensions

import kotlin.math.PI

// Function to convert degrees to radians
fun Float.degreesToRadians(): Float {
    return this * (PI / 180.0).toFloat()
}

fun Int.isDivisible(divisor: Int): Boolean {
    return divisor != 0 && this % divisor == 0
}

fun Int.isDivisible2(divisor: Int): Boolean {
    return divisor != 0 && this % 4 == 0
}

fun Long.toRange0To360(): Float {
    return this / 1000f * (360f / 60f)
}
