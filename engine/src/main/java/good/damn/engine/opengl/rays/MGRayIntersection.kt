package good.damn.engine.opengl.rays

import good.damn.engine.sdk.SDVector3

class MGRayIntersection {

    fun intersect(
        position: SDVector3,
        direction: SDVector3,
        outResult: SDVector3
    ) {
        outResult.x = position.x + direction.x * 200f
        outResult.y = position.y + direction.y * 200f
        outResult.z = position.z + direction.z * 200f
    }

}