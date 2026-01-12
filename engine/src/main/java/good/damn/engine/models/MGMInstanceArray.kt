package good.damn.engine.models

import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal

data class MGMInstanceArray(
    val vertexArray: MGArrayVertexInstanced,
    val modelMatrices: List<
        COMatrixTransformationNormal<COMatrixScaleRotation>
    >
)