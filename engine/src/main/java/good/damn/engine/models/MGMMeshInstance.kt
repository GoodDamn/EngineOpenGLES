package good.damn.engine.models

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation

data class MGMMeshInstance(
    val vertexArray: MGArrayVertex,
    val obj: MGObject3d,
    val matrices: Array<MGMatrixScaleRotation>
)