package jh.portfinder.model

class PointGroup (points: Collection<Pair<Double, Double>>) {
    init {
        addPoints(points)
    }

    constructor(point: Pair<Double, Double>) : this(setOf(point))

    val points: MutableSet<Pair<Double, Double>> = mutableSetOf()
        get() {
            return field
        }

    fun getCenter(): Pair<Double, Double> {
        val x = points.map { it.first }.average()
        val y = points.map { it.second }.average()
        return Pair(x, y)
    }

    fun addPoints(points: Collection<Pair<Double, Double>>) {
        if (points.isEmpty()) return
        this.points.addAll(points)
    }

    fun addPoint(point: Pair<Double, Double>) {
        this.points.add(point)
    }



}