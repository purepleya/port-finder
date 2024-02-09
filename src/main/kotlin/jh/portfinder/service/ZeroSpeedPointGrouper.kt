package jh.portfinder.service

import jh.portfinder.data.repository.dailyzerospeedpoint.DailyZeroSpeedPointRepository
import jh.portfinder.data.repository.zerospeedpointgroup.ZeroSpeedPointGroupRepository
import jh.portfinder.logger
import jh.portfinder.model.PointGroup
import jh.portfinder.util.DateProgression
import jh.portfinder.util.EarthDistanceCalculator
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class
ZeroSpeedPointGrouper (
    private val dailyZeroSpeedPointRepository: DailyZeroSpeedPointRepository,
    private val zeroSpeedPointGroupRepository: ZeroSpeedPointGroupRepository,
    private val zeroSpeedPointGroupMerger: ZeroSpeedPointGroupMerger
) {
    companion object {
        const val LIMIT_DISTANCE = 0.3 // km
    }

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)
    private val log = logger<ZeroSpeedPointGrouper>()


    fun group(from: LocalDate, to: LocalDate) {
        var groups = readOldGroups()

        for (date in from..to) {
            groups = group(groups, date)
        }
        groups = zeroSpeedPointGroupMerger.mergeGroups(groups)
        write(from, to, groups)
    }

    private fun readOldGroups(): List<PointGroup> {
        val groups = mutableListOf<PointGroup>()
        zeroSpeedPointGroupRepository.getGroupList().forEach {
            val points = zeroSpeedPointGroupRepository.getGroupPoints(it)
            groups.add(PointGroup(points))
        }
        return groups
    }

    private fun group(groups: List<PointGroup>, date: LocalDate): List<PointGroup> {
        log.info("Grouping zero speed points for $date")

        val mutableGroupList = groups.toMutableList()
        val points = dailyZeroSpeedPointRepository
            .read(date)
            .sortedWith(compareBy({ it.first }, { it.second }))

        for (point in points) {
            var found = false
            for (group in mutableGroupList) {
                if (group.points.any {
                        EarthDistanceCalculator.distance(
                            it.second,
                            it.first,
                            point.second,
                            point.first
                        ) < LIMIT_DISTANCE
                    }) {
                    group.addPoint(point)
                    found = true
                    break
                }
            }

            if (!found) {
                mutableGroupList.add(PointGroup(point))
            }
        }

        return groups
    }

    private fun write(from: LocalDate, to: LocalDate, groups: Collection<PointGroup>) {
        zeroSpeedPointGroupRepository.clean()
        zeroSpeedPointGroupRepository.write(groups.map { it.points })
    }

}