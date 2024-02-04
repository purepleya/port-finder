package jh.portfinder.util

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EarthDistanceCalculatorTest {

    @Test
    fun distance_test() {
        val distance = EarthDistanceCalculator.distance(0.0, 0.0, 0.43, 0.21)
        assertThat(distance).isCloseTo(53.0, Offset.offset(0.4))
    }
}