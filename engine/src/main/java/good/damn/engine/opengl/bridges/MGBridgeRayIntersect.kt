package good.damn.engine.opengl.bridges

import good.damn.engine.sdk.SDVector3
import good.damn.engine.opengl.triggers.MGIMatrixTrigger

data class MGBridgeRayIntersect(
    var intersectUpdate: MGIRayIntersectUpdate? = null,
    val outPointLead: SDVector3 = SDVector3(0f),
    var distance: Float = 200f
)