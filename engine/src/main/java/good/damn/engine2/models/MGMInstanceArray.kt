package good.damn.engine2.models

import good.damn.apigl.arrays.GLArrayVertexInstanced
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal

data class MGMInstanceArray(
    val vertexArray: GLArrayVertexInstanced,
    val modelMatrices: List<
        COMatrixTransformationNormal<COMatrixScaleRotation>
    >
)