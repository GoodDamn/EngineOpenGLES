package good.damn.engine.opengl.rays

import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.entities.MGLandscape

class MGRayIntersection(
    private val landscape: MGLandscape
) {

    fun intersect(
        position: MGVector,
        direction: MGVector,
        outResult: MGVector
    ) {
        landscape.intersect(
            position,
            direction,
            outResult
        )
    }

}