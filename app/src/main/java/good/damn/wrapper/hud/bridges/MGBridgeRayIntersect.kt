package good.damn.wrapper.hud.bridges

import good.damn.engine.sdk.SDVector3

data class MGBridgeRayIntersect(
    var intersectUpdate: MGIRayIntersectUpdate? = null,
    val outPointLead: SDVector3 = SDVector3(0f),
    var distance: Float = 200f
)