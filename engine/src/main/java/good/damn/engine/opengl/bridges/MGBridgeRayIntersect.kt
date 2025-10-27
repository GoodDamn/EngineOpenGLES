package good.damn.engine.opengl.bridges

import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.triggers.MGIMatrixTrigger
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh

data class MGBridgeRayIntersect(
    var matrix: MGIMatrixTrigger? = null,
    val outPointLead: MGVector = MGVector(0f)
)