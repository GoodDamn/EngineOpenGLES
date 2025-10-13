package good.damn.engine.opengl.bridges

import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.matrices.MGMatrixScale

data class MGBridgeRayIntersect(
    var matrix: MGMatrixScale? = null,
    val outPointLead: MGVector = MGVector(0f)
)