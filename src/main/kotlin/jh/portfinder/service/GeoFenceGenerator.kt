package jh.portfinder.service

import jh.portfinder.data.repository.geofence.GeoFenceRepository
import jh.portfinder.data.repository.zerospeedpointgroup.ZeroSpeedPointGroupRepository
import org.apache.commons.math3.analysis.function.Min
import org.apache.commons.math3.stat.regression.SimpleRegression
import org.springframework.stereotype.Service

@Service
class GeoFenceGenerator (
    private val zeroSpeedPointGroupRepository: ZeroSpeedPointGroupRepository,
    private val geoFenceRepository: GeoFenceRepository
) {

    fun generateGeoFence() {
        clean()
        val groupNames = getGroupList()

        for (groupName in groupNames) {
            val geoFence = generateGeoFence(groupName)

            if (geoFence.isNotEmpty()) {
                write(groupName, geoFence)
            }
        }
    }

    fun generateGeoFence(groupName: String): List<Pair<Double, Double>> {
        val points = getPoints(groupName)
        if (hasEnoughPoints(points)) {
            val geoFence = generate(points)
            return geoFence
        } else {
            return emptyList()
        }
    }

    private fun clean() {
        geoFenceRepository.clean()
    }

    private fun hasEnoughPoints(points: List<Pair<Double, Double>>): Boolean {
        return points.size >= 5
    }

    private fun getGroupList(): List<String> {
        return zeroSpeedPointGroupRepository.getGroupList()
    }

    private fun getPoints(groupName: String): List<Pair<Double, Double>> {
        return zeroSpeedPointGroupRepository.getGroupPoints(groupName)
    }

    private fun generate(points: List<Pair<Double, Double>>): List<Pair<Double, Double>> {
        val (slope, intercept) = getRegressionFactors(points)
        val geoFence = generatePolygon(slope, intercept, points)
        return geoFence
    }

    private fun getRegressionFactors(points: List<Pair<Double, Double>>): Pair<Double, Double> {
        SimpleRegression().apply {
            points.forEach { addData(it.first, it.second) }
        }.let {
            return Pair(it.slope, it.intercept)
        }
    }

    private fun generatePolygon(slope: Double, intercept: Double, points: List<Pair<Double, Double>>): List<Pair<Double, Double>> {
        var maxYDistance = 0.0
        var minX = 180.0
        var maxX = -180.0

        points.forEach {
            val x = it.first
            val y = it.second
            val regressionY = slope * x + intercept
            val yDistance = Math.abs(y - regressionY)

            if (yDistance > maxYDistance) {
                maxYDistance = yDistance
            }

            if (x < minX) {
                minX = x
            }
            if (x > maxX) {
                maxX = x
            }
        }

        val width = Math.abs(maxX - minX)
        
        // 좌측 하단 꼭지점 부터 시계방향 (원래 마름모로 만들어야 하는데, 일단 평행사변형으로 만듬)
        val x1 = minX - getBufferLength(width)
        val y1 = slope * x1 + intercept - maxYDistance - getBufferLength(maxYDistance)

//        원래 regression equation 그래프에 직교하는 그래프를 yc = -1/slope * x + b 라고 했을때
//        아래 그래프와 직교하는 그래프의 b 값은
//         b = y1 - (-1 / slope) * x1
//        위 그래프와 직교하는 그래프의 x 좌표를 구하려면
//        직교하는 그래프                             원래 그래프
//        -1/slope * x + (y1 - (-1 / slope) * x1) = slope * x + intercept + maxYDistance + getBufferLength(maxYDistance)
//        -1/slope * x - slope * x  = intercept + maxYDistance + getBufferLength(maxYDistance) - y1 + (-1 / slope) * x1
//        (-1/slope - slope) * x = intercept + maxYDistance + getBufferLength(maxYDistance) - y1 + (-1 / slope) * x1
//        x = (intercept + maxYDistance + getBufferLength(maxYDistance) - y1 + (-1 / slope) * x1) / (-1/slope - slope)

        val x2 = (intercept + maxYDistance + getBufferLength(maxYDistance) - y1 + (-1 / slope) * x1) / (-1/slope - slope)
        val y2 = slope * x2 + intercept + maxYDistance + getBufferLength(maxYDistance)

        val x3 = maxX + getBufferLength(width)
        val y3 = slope * x3 + intercept + maxYDistance + getBufferLength(maxYDistance)

        val x4 = x1 + Math.abs(x3 - x2)
        val y4 = slope * x4 + intercept - maxYDistance - getBufferLength(maxYDistance)

        return listOf(
            Pair(x1, y1),
            Pair(x2, y2),
            Pair(x3, y3),
            Pair(x4, y4)
        )
    }

    fun getBufferLength(length: Double): Double {
        val bufferRatio = 0.3
        return length * bufferRatio
    }

    private fun write(groupName: String, geoFence: Collection<Pair<Double, Double>>) {
        geoFenceRepository.write(groupName, geoFence)
    }


}