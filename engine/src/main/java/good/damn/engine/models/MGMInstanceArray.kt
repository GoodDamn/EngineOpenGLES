package good.damn.engine.models

import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal

data class MGMInstanceArray(
    val vertexArray: MGArrayVertexInstanced,
    val modelMatrices: List<
        MGMatrixTransformationNormal<MGMatrixScaleRotation>
    >
)