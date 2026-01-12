package good.damn.engine.models

import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.common.matrices.MGMatrixScaleRotation
import good.damn.common.matrices.MGMatrixTransformationNormal

data class MGMInstanceArray(
    val vertexArray: MGArrayVertexInstanced,
    val modelMatrices: List<
        MGMatrixTransformationNormal<MGMatrixScaleRotation>
    >
)