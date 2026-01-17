package good.damn.engine.models

import good.damn.apigl.arrays.MGArrayVertexInstanced
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal

data class MGMInstanceArray(
    val vertexArray: good.damn.apigl.arrays.MGArrayVertexInstanced,
    val modelMatrices: List<
        COMatrixTransformationNormal<COMatrixScaleRotation>
    >
)