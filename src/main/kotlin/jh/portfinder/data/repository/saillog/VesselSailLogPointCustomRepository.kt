package jh.portfinder.data.repository.saillog

import java.time.LocalDateTime

interface VesselSailLogPointCustomRepository {
    fun queryZeroSpeedPointByVessels(from: LocalDateTime, to: LocalDateTime, reportCount: Int): List<ZeroSpeedPointByVessel>
}