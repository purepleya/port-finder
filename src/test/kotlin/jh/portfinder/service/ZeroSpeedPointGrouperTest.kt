package jh.portfinder.service

import jh.portfinder.util.DateProgression
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import java.time.LocalDate

class ZeroSpeedPointGrouperTest {

    @Test
    fun localdate_range_test() {
        operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)
        val from = LocalDate.of(2024, 1, 1)
        val to = LocalDate.of(2024, 1, 4)

        var result: String = ""
        for (date in from..to) {
            result += date.toString() + ","
        }

        assertThat(result).isEqualTo("2024-01-01,2024-01-02,2024-01-03,2024-01-04,")
    }

}