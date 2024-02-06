package jh.portfinder.data.repository.geofence

interface GeoFenceRepository {
    fun write(groupName: String, points: Collection<Pair<Double, Double>>)
    fun clean()

    fun read(from: Int, to: Int): List<List<Pair<Double, Double>>>

}