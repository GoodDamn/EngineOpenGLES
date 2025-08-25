package good.damn.engine.opengl.rays

import good.damn.engine.opengl.MGVector

class MGRayIntersection {

    fun intersect(
        position: MGVector,
        direction: MGVector,
        outResult: MGVector
    ) {
        outResult.x = position.x + direction.x * 10f
        outResult.y = position.y + direction.y * 10f
        outResult.z = position.z + direction.z * 10f
    }

}