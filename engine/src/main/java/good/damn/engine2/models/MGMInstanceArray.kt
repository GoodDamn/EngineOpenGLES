package good.damn.engine2.models

import good.damn.apigl.arrays.GLArrayVertexInstanced
import good.damn.engine.sdk.matrices.SDMatrixScaleRotation
import good.damn.engine.sdk.matrices.SDMatrixTransformationNormal

data class MGMInstanceArray(
    val vertexArray: GLArrayVertexInstanced,
    val modelMatrices: List<
        SDMatrixTransformationNormal<SDMatrixScaleRotation>
    >
)