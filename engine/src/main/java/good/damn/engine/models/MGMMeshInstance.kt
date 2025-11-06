package good.damn.engine.models

import good.damn.engine.opengl.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation

data class MGMMeshInstance(
    val vertexArray: MGArrayVertexInstanced,
    val material: MGMaterial,
    val matrices: Array<MGMatrixScaleRotation>
)