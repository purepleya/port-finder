package jh.portfinder.data.repository

import jh.portfinder.data.repository.saillog.VesselSailLogPointCustomRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.Month

@SpringBootTest
class VesselSailLogPointCustomRepositoryImplTest {

    @Autowired
    private lateinit var vesselSailLogPointCustomRepository: VesselSailLogPointCustomRepositoryImpl


    @Test
    fun queryZeroSpeedPointByVesselsTest() {
        val from = LocalDateTime.of(2024, Month.of(1), 1, 0, 0, 0)
        val to = from.plusHours(2);
        val reportCount = 3
        val result = vesselSailLogPointCustomRepository.queryZeroSpeedPointByVessels(from, to, reportCount)

        assertThat(result.size).isEqualTo(621)

        assertThat(result.filter { it.mmsi.equals(2339007) }.size).isEqualTo(1)
        assertThat(result.filter { it.mmsi.equals(2339009) }.size).isEqualTo(1)
        assertThat(result.filter { it.mmsi.equals(211265530) }.size).isEqualTo(1)
        assertThat(result.filter { it.mmsi.equals(211366340) }.size).isEqualTo(1)
        assertThat(result.filter { it.mmsi.equals(710010820) }.size).isEqualTo(1)
        assertThat(result.filter { it.mmsi.equals(730088000) }.size).isEqualTo(1)
        assertThat(result.filter { it.mmsi.equals(900223504) }.size).isEqualTo(1)
    }

}