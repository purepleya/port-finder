package jh.portfinder.data.repository.geofence

interface GeoFenceRepository {
    fun write(groupName: String, points: Collection<Pair<Double, Double>>)
    fun clean()

    fun read(): List<List<Pair<Double, Double>>>

}