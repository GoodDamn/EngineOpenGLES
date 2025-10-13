package good.damn.engine.opengl.bridges

import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh

data class MGBridgeRayIntersect(
    var matrix: MGMatrixTriggerMesh? = null,
    val outPointLead: MGVector = MGVector(0f)
)