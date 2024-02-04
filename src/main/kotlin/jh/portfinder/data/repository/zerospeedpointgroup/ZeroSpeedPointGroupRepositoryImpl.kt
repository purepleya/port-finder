package jh.portfinder.data.repository.zerospeedpointgroup

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path

@Service
class ZeroSpeedPointGroupRepositoryImpl (
    @Value("\${zero-speed-point-group-repository-path}") private val repositoryPath: String
): ZeroSpeedPointGroupRepository {

    override fun write(groups: Collection<Set<Pair<Double, Double>>>) {
        val directoryPath = Path(repositoryPath)
        Files.list(directoryPath).forEach { Files.deleteIfExists(it) }
        Files.deleteIfExists(directoryPath)
        Files.createDirectories(directoryPath)

        for (i in 0..(groups.size - 1)) {
            val group = groups.elementAt(i)
            val filePath = Path("$repositoryPath/group-$i")

            Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).use { writer ->
                for (point in group) {
                    writer.write("${point.first},${point.second}\n")
                }
            }
        }
    }
}