package utils

import java.util.*

object Utils {
    private val random = Random()
    val numberGenerator: () -> Double = { random.nextDouble() }
}