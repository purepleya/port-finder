package jh.portfinder.data.repository.zerospeedpointgroup

interface ZeroSpeedPointGroupRepository {
    fun write(groups: Collection<Set<Pair<Double, Double>>>)
}