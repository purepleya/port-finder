package jh.portfinder.data.repository.dailyzerospeedpoint

import org.locationtech.jts.geom.Point
import java.time.LocalDate

interface DailyZeroSpeedPointRepository {
    fun clean(date: LocalDate)
    fun write(date: LocalDate, points: Collection<Pair<Double, Double>>)
    fun read(date: LocalDate): List<Pair<Double, Double>>
}