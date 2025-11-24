package good.damn.engine.opengl.rays

import good.damn.engine.sdk.MGVector3

class MGRayIntersection {

    fun intersect(
        position: MGVector3,
        direction: MGVector3,
        outResult: MGVector3
    ) {
        outResult.x = position.x + direction.x * 200f
        outResult.y = position.y + direction.y * 200f
        outResult.z = position.z + direction.z * 200f
    }

}