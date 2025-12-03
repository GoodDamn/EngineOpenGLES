package good.damn.engine.models

import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.shaders.MGShaderOpaque

data class MGMInstanceMesh(
    val shader: MGShaderOpaque,
    val vertexArray: MGArrayVertexInstanced,
    val material: MGMaterial,
    val enableCullFace: Boolean,
    val matrices: Array<
        MGMatrixTransformationNormal<
            MGMatrixScaleRotation
        >
    >
)