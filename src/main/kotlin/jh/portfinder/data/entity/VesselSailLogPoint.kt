package jh.portfinder.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import lombok.EqualsAndHashCode
import org.locationtech.jts.geom.Point
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "vessel_sail_log_point")
@IdClass(VesselSailLogPoint.Companion.VesselSailLogPointId::class)
class VesselSailLogPoint {

    @Id
    @Column(name = "mmsi")
    var mmsi: Int? = null

    @Column(name = "nav_code")
    var navCode: Int? = null

    @Column(name = "speed", columnDefinition = "numeric")
    var speed: Double? = null

    @Column(name = "heading", columnDefinition = "numeric")
    var heading: Float? = null

    @Column(name = "turn", columnDefinition = "numeric")
    var turn: Float? = null

    @Column(name = "course", columnDefinition = "numeric")
    var course: Float? = null

    @Column(name = "geom", columnDefinition = "geometry(Point,4326)")
    var geom: Point? = null

    @Id
    @Column(name = "received_date_time")
    var receivedDateTime: LocalDateTime? = null


    companion object {
        @EqualsAndHashCode
        class VesselSailLogPointId : Serializable {
            var mmsi: Int? = null
            var receivedDateTime: String? = null
        }
    }


}