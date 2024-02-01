package jh.portfinder.data.repository

import jh.portfinder.data.entity.VesselSailLogPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VesselSailLogPointRepository: JpaRepository<VesselSailLogPoint, VesselSailLogPoint.Companion.VesselSailLogPointId>
                                        , VesselSailLogPointCustomRepository {
}