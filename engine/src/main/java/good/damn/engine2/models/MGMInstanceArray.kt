package good.damn.engine2.models

import good.damn.apigl.arrays.GLArrayVertexInstanced
import good.damn.engine.sdk.matrices.COMatrixScaleRotation
import good.damn.engine.sdk.matrices.COMatrixTransformationNormal

data class MGMInstanceArray(
    val vertexArray: GLArrayVertexInstanced,
    val modelMatrices: List<
        COMatrixTransformationNormal<COMatrixScaleRotation>
    >
)