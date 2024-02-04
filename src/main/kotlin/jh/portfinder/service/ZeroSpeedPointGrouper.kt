package jh.portfinder.service

import jh.portfinder.data.repository.dailyzerospeedpoint.DailyZeroSpeedPointRepository
import jh.portfinder.util.DateProgression
import jh.portfinder.util.EarthDistanceCalculator
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ZeroSpeedPointGrouper (
    private val dailyZeroSpeedPointRepository: DailyZeroSpeedPointRepository
) {
    val LIMIT_DISTANCE = 0.5

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)



    fun group(from: LocalDate, to: LocalDate) {
        val points = read(from, to)
        val groups: List<Set<Pair<Double, Double>>> = group(points)
        write(groups)
    }


    private fun read(from: LocalDate, to: LocalDate): Set<Pair<Double, Double>> {
        val points = HashSet<Pair<Double, Double>> ()

        for (date in from..to) {
            points.addAll(dailyZeroSpeedPointRepository.read(date))
        }

        return points
    }


    private fun group(points: Collection<Pair<Double, Double>>): List<Set<Pair<Double, Double>>> {
        val pointList = points.toMutableList()
                            .sortedWith(compareBy({ it.first }, { it.second }))


        val pointGroups = mutableListOf<Set<Pair<Double, Double>>>()

        var lastPoint: Pair<Double, Double>? = null
        var lastGroup: MutableSet<Pair<Double, Double>>? = null

        for (point in pointList) {
            if (lastPoint == null) {
                lastGroup = mutableSetOf(point)
            } else if (EarthDistanceCalculator.distance(lastPoint.second, lastPoint.first, point.second, point.first) < LIMIT_DISTANCE) {
                lastGroup?.add(point)
            } else {
                lastGroup?.let { pointGroups.add(it) }
                lastGroup = mutableSetOf(point)
            }

            lastPoint = point
        }

        return pointGroups
    }


    private fun write(groups: Collection<Set<Pair<Double, Double>>>) {
        println(groups)
    }

}