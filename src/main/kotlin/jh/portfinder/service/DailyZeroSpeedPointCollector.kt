package jh.portfinder.service

import jh.portfinder.data.repository.dailyzerospeedpoint.DailyZeroSpeedPointRepository
import jh.portfinder.data.repository.saillog.VesselSailLogPointRepository
import jh.portfinder.data.repository.saillog.ZeroSpeedPointByVessel
import jh.portfinder.logger
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class DailyZeroSpeedPointCollector (
    private val vesselSailLogPointRepository: VesselSailLogPointRepository,
    private val dailyZeroSpeedPointRepository: DailyZeroSpeedPointRepository
) {

    private val log = logger<DailyZeroSpeedPointCollector>()
    private val hourRange: Int = 3
    private val reportCount: Int = 3

    fun collect(date: LocalDate) {
        cleanOldData(date)
        for (hour in 0..23 step hourRange) {
            val zeroSpeedPointsByVessel = read(date, hour)
            val points = zeroSpeedPointsByVessel
                                        .mapNotNull { it.coordinate?.let { it1 -> Pair<Double, Double>(it1.x, it.coordinate.y) } }
                                        .toSet()
            write(date, points.toList())
            Thread.sleep(5000)
        }
    }

    private fun cleanOldData(date: LocalDate) {
        dailyZeroSpeedPointRepository.clean(date)
    }

    private fun read(date: LocalDate, hour: Int): List<ZeroSpeedPointByVessel> {
        val from = LocalDateTime.of(date, LocalTime.of(hour, 0))
        val to = from.plusHours(this.hourRange.toLong())

        return vesselSailLogPointRepository.queryZeroSpeedPointByVessels(from, to, reportCount)
    }

    private fun write(date: LocalDate, points: Collection<Pair<Double, Double>>) {
        try {
            dailyZeroSpeedPointRepository.write(date, points)
        } catch(e: Exception) {
            log.error(e.message, e.cause)
        }
    }
}