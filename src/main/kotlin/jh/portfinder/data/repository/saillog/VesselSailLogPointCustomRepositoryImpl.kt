package jh.portfinder.data.repository.saillog

import com.querydsl.core.annotations.QueryProjection
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import jh.portfinder.data.entity.QVesselSailLogPoint.vesselSailLogPoint
import lombok.RequiredArgsConstructor
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@RequiredArgsConstructor
@Repository
class VesselSailLogPointCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): VesselSailLogPointCustomRepository {

    override fun queryZeroSpeedPointByVessels(from: LocalDateTime, to: LocalDateTime, reportCount: Int): List<ZeroSpeedPointByVessel> {

        val result = jpaQueryFactory.select(QZeroSpeedPointByVessel(vesselSailLogPoint.mmsi, vesselSailLogPoint.geom, Expressions.constant(from), Expressions.constant(to)))
            .from(vesselSailLogPoint)
            .where(vesselSailLogPoint.speed.eq(0.0)
                .and(vesselSailLogPoint.receivedDateTime.goe(from))
                .and(vesselSailLogPoint.receivedDateTime.lt(to)))
            .groupBy(vesselSailLogPoint.mmsi, vesselSailLogPoint.geom)
            .having(vesselSailLogPoint.speed.count().gt(reportCount))
            .fetch()
        return result;
    }
}



data class ZeroSpeedPointByVessel @QueryProjection constructor(
                                                        val mmsi: Int
                                                        , val coordinate: Point?
                                                        , val receivedDateTimeFrom: LocalDateTime
                                                        , val receivedDateTimeTo: LocalDateTime)