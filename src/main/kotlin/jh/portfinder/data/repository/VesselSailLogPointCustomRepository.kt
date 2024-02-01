package jh.portfinder.data.repository

import org.springframework.stereotype.Repository
import java.time.LocalDateTime

interface VesselSailLogPointCustomRepository {
    fun queryZeroSpeedPointByVessels(from: LocalDateTime, to: LocalDateTime, reportCount: Int): List<ZeroSpeedPointByVessel>
}