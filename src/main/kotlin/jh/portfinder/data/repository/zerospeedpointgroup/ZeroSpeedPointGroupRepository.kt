package jh.portfinder.data.repository.zerospeedpointgroup

interface ZeroSpeedPointGroupRepository {
    fun write(groups: Collection<Set<Pair<Double, Double>>>)

    fun getGroupList(): List<String>

    fun getGroupPoints(group: String): List<Pair<Double, Double>>
}