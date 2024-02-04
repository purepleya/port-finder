package jh.portfinder.data.repository.dailyzerospeedpoint

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.time.LocalDate
import kotlin.io.path.Path

class DailyZeroSpeedPointRepositoryImplTest {

    private val filePath:String = "/port-finder/test/zero-speed-point-repository"

    @Test
    fun files_function_test() {
        val path = Path(filePath)
        val fileAndPath = Path(filePath + "/2021-01-01")

        assertThat(Files.exists(path)).isFalse()

        Files.createDirectories(path)

        assertThat(Files.exists(path)).isTrue()

        assertThat(Files.exists(fileAndPath)).isFalse()

        Files.createFile(fileAndPath)

        assertThat(Files.exists(fileAndPath)).isTrue()

        Files.delete(fileAndPath)

        assertThat(Files.exists(fileAndPath)).isFalse()

        Files.delete(path)
    }


    @Test
    fun write_read_test() {
        val repository = DailyZeroSpeedPointRepositoryImpl(filePath)

        val date = LocalDate.of(2024, 1, 1)

        val points = listOf(Pair(1.0, 2.0), Pair(3.0, 4.0))

        repository.write(date, points)

        val readPoints = repository.read(date)

        assertThat(readPoints).isEqualTo(points)

        repository.clean(date)

        val path = Path(filePath)
        Files.delete(path)
    }

}