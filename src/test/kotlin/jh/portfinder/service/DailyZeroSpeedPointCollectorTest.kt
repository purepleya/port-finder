package jh.portfinder.service

import jh.portfinder.data.repository.dailyzerospeedpoint.DailyZeroSpeedPointRepository
import jh.portfinder.data.repository.saillog.VesselSailLogPointRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Files
import java.time.LocalDate
import kotlin.io.path.Path

@SpringBootTest
class DailyZeroSpeedPointCollectorTest {

    @Autowired
    private lateinit var dailyZeroSpeedPointCollector: DailyZeroSpeedPointCollector

    @Value("\${zero-speed-point-repository-path}")
    private lateinit var repositoryPath: String


    @Test
    fun collect_unit_test() {
        val vesselSailLogPointRepository: VesselSailLogPointRepository = Mockito.mock(VesselSailLogPointRepository::class.java)
        val dailyZeroSpeedPointRepository: DailyZeroSpeedPointRepository = Mockito.mock(DailyZeroSpeedPointRepository::class.java)
        val collector = DailyZeroSpeedPointCollector(vesselSailLogPointRepository, dailyZeroSpeedPointRepository)

        // Given

        Mockito.`when`(vesselSailLogPointRepository.queryZeroSpeedPointByVessels(any(), any(), any()))
                .thenReturn(listOf())

        // When
        collector.collect(LocalDate.now())

        // Then
        Mockito.verify(dailyZeroSpeedPointRepository, Mockito.times(8)).write(any(), any())
    }


    @Test
    fun collect_integration_test() {
        val date = LocalDate.now().minusDays(2)
        dailyZeroSpeedPointCollector.collect(date)
        val filePath = "$repositoryPath/$date"
        assertThat(Files.exists(Path(filePath))).isTrue()
    }


}
