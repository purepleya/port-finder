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

    override fun read(from: Int, to: Int): List<List<Pair<Double, Double>>> {
        val result = mutableListOf<List<Pair<Double, Double>>>()

        for (i in from..to - 1) {
            val filePath = Path("$repositoryPath/group-$i")
            if (Files.exists(filePath).not()) {
                continue
            }
            val points = Files.readAllLines(filePath).map { line ->
                val (x, y) = line.split(",")
                Pair(x.toDouble(), y.toDouble())
            }

            result.add(points)
        }

        return result;
    }
}