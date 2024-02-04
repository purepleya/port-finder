package jh.portfinder.service

import jh.portfinder.data.repository.dailyzerospeedpoint.DailyZeroSpeedPointRepository
import jh.portfinder.data.repository.zerospeedpointgroup.ZeroSpeedPointGroupRepository
import jh.portfinder.util.DateProgression
import jh.portfinder.util.EarthDistanceCalculator
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ZeroSpeedPointGrouper (
    private val dailyZeroSpeedPointRepository: DailyZeroSpeedPointRepository,
    private val zeroSpeedPointGroupRepository: ZeroSpeedPointGroupRepository
) {
    val LIMIT_DISTANCE = 0.6
    val MINIMUM_POINT_COUNT = 3

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)



    fun group(from: LocalDate, to: LocalDate) {
        val points = read(from, to)
        val groups: List<Set<Pair<Double, Double>>> = group(points)
        val filteredGroups: List<Set<Pair<Double, Double>>> = groups.filter { it.size >= MINIMUM_POINT_COUNT }
        write(from, to, filteredGroups)
    }


    private fun read(from: LocalDate, to: LocalDate): Set<Pair<Double, Double>> {
        val points = HashSet<Pair<Double, Double>> ()

        for (date in from..to) {
            points.addAll(dailyZeroSpeedPointRepository.read(date))
        }

        return points
    }


    private fun group(points: Collection<Pair<Double, Double>>): List<Set<Pair<Double, Double>>> {
        val sortedPoints = points.sortedWith(compareBy({ it.first }, { it.second }))
        val pointGroups = mutableListOf<MutableSet<Pair<Double, Double>>>()

        for (point in sortedPoints) {
            var found = false
            for (group in pointGroups) {
                if (group.any { EarthDistanceCalculator.distance(it.second, it.first, point.second, point.first) < LIMIT_DISTANCE }) {
                    group.add(point)
                    found = true
                    break
                }
            }

            if (!found) {
                pointGroups.add(mutableSetOf(point))
            }
        }

        return pointGroups
    }


    private fun write(from: LocalDate, to: LocalDate, groups: Collection<Set<Pair<Double, Double>>>) {
        zeroSpeedPointGroupRepository.write(groups)
    }

}