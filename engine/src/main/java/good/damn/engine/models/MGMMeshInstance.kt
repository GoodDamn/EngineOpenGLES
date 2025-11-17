package good.damn.engine.models

import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal

data class MGMMeshInstance(
    val vertexArray: MGArrayVertexInstanced,
    val material: MGMaterial,
    val matrices: Array<
        MGMatrixTransformationNormal<
            MGMatrixScaleRotation
        >
    >
)