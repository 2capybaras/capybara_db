package util

import java.util.Random

object Utils {
    private val random = Random()
    val numberGenerator: () -> Double = { random.nextDouble() }
}