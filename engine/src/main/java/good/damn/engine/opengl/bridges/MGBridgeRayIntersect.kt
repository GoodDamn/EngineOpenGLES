package good.damn.engine.opengl.bridges

import good.damn.engine.sdk.MGVector3
import good.damn.engine.opengl.triggers.MGIMatrixTrigger

data class MGBridgeRayIntersect(
    var matrix: MGIMatrixTrigger? = null,
    val outPointLead: MGVector3 = MGVector3(0f)
)