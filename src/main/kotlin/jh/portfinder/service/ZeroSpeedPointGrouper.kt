package jh.portfinder.service

import jh.portfinder.data.repository.dailyzerospeedpoint.DailyZeroSpeedPointRepository
import jh.portfinder.data.repository.zerospeedpointgroup.ZeroSpeedPointGroupRepository
import jh.portfinder.logger
import jh.portfinder.util.DateProgression
import jh.portfinder.util.EarthDistanceCalculator
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ZeroSpeedPointGrouper (
    private val dailyZeroSpeedPointRepository: DailyZeroSpeedPointRepository,
    private val zeroSpeedPointGroupRepository: ZeroSpeedPointGroupRepository
) {
    val LIMIT_DISTANCE = 0.4 // km
    val MINIMUM_POINT_COUNT = 10 // minimum number of points to form a group

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)
    private val log = logger<ZeroSpeedPointGrouper>()


    fun group(from: LocalDate, to: LocalDate) {
        val groups: MutableList<MutableSet<Pair<Double, Double>>> = mutableListOf()

        for (date in from..to) {
            log.info("Grouping zero speed points for $date")
            val points = dailyZeroSpeedPointRepository
                            .read(date)
                            .sortedWith(compareBy({ it.first }, { it.second }))

            for (point in points) {
                var found = false
                for (group in groups) {
                    if (group.any { EarthDistanceCalculator.distance(it.second, it.first, point.second, point.first) < LIMIT_DISTANCE }) {
                        group.add(point)
                        found = true
                        break
                    }
                }

                if (!found) {
                    groups.add(mutableSetOf(point))
                }
            }
        }

        val filteredGroups: List<Set<Pair<Double, Double>>> = groups.filter { it.size >= MINIMUM_POINT_COUNT }
        write(from, to, filteredGroups)
    }


    private fun write(from: LocalDate, to: LocalDate, groups: Collection<Set<Pair<Double, Double>>>) {
        zeroSpeedPointGroupRepository.write(groups)
    }

}