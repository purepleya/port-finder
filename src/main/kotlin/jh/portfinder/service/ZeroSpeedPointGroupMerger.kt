package jh.portfinder.service

import jh.portfinder.logger
import jh.portfinder.model.PointGroup
import jh.portfinder.util.EarthDistanceCalculator
import jh.portfinder.util.EarthDistanceCalculator.Companion.distance
import org.springframework.stereotype.Service

@Service
class ZeroSpeedPointGroupMerger {
    private val log = logger<ZeroSpeedPointGroupMerger>()
    fun mergeGroups(groups: List<PointGroup>): List<PointGroup> {
//        TODO("성능 개선 필요 - 그룹을 중심좌표 기준으로 정렬하고 중심좌표가 어느정도 비슷한 놈들끼리만 비교하도록")
        log.info("Merging groups")
        var mergedGroups: MutableList<PointGroup> = mutableListOf()

        if (groups.isEmpty()) {
            return mergedGroups
        }

        for (group in groups) {
            var found = false
            for (mergedGroup in mergedGroups) {
                if (mergedGroup.points.any {mg -> group.points.any { distance(it.second, it.first, mg.second, mg.first) < ZeroSpeedPointGrouper.Companion.LIMIT_DISTANCE } }) {
                    mergedGroup.addPoints(group.points)
                    found = true
                    break
                }
            }

            if (!found) {
                mergedGroups.add(group)
            }
        }

        return mergedGroups
    }
}