package jh.portfinder.data.repository.geofence

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path

@Service
class GeoFenceRepositoryImpl(
    @Value("\${geofence-repository-path}") private val repositoryPath: String
): GeoFenceRepository {
    override fun write(groupName: String, points: Collection<Pair<Double, Double>>) {
        val path = Path("$repositoryPath")
        if (Files.exists(path).not()) {
            Files.createDirectories(path)
        }

        val filePath = Path("$repositoryPath/$groupName")
        Files.deleteIfExists(filePath)

        Files.newBufferedWriter(filePath, StandardOpenOption.CREATE).use { writer ->
            points.forEach {
                writer.write("${it.first},${it.second}\n")
            }
        }
    }

    override fun clean() {
        val path = Path("$repositoryPath")
        Files.list(path).forEach {
            Files.deleteIfExists(it)
        }
    }

    override fun read(): List<List<Pair<Double, Double>>> {
        val path = Path("$repositoryPath")
        return Files.list(path).map {
            Files.readAllLines(it).map { line ->
                val (x, y) = line.split(",")
                Pair(x.toDouble(), y.toDouble())
            }
        }.toList()
    }
}