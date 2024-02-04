package jh.portfinder.data.repository.dailyzerospeedpoint

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import kotlin.io.path.Path

@Service
class DailyZeroSpeedPointRepositoryImpl (
    @Value("\${zero-speed-point-repository-path}") private val repositoryPath: String
): DailyZeroSpeedPointRepository {


    private fun getDirectoryPath(): Path {
        return Path(repositoryPath)
    }

    private fun getFilePath(date: LocalDate): Path {
        return Path("$repositoryPath/${date}")
    }

    override fun clean(date: LocalDate) {
        val path = getFilePath(date)
        if (Files.exists(path)) {
            Files.delete(path)
        }
    }


    override fun write(date: LocalDate, points: Collection<Pair<Double, Double>>) {
        val directoryPath = getDirectoryPath()

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath)
        }

        Files.newBufferedWriter(getFilePath(date), StandardOpenOption.CREATE, StandardOpenOption.APPEND).use { writer ->
            points.forEach {
                writer.write("${it.first},${it.second}")
                writer.newLine()
            }
        }
    }

    override fun read(date: LocalDate): List<Pair<Double, Double>> {
        val filePath = getFilePath(date)
        if (!Files.exists(filePath)) {
            return emptyList()
        }

        Files.newBufferedReader(filePath).use { reader ->
            return reader.readLines().map {
                val (x, y) = it.split(",")
                Pair(x.toDouble(), y.toDouble())
            }
        }
    }

}