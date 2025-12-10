package good.damn.engine.models

import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderOpaqueDefer

data class MGMInstanceMesh(
    val shader: MGShaderOpaqueDefer,
    val vertexArray: MGArrayVertexInstanced,
    val material: Array<MGMaterial>,
    val enableCullFace: Boolean,
    val matrices: List<
        MGMatrixTransformationNormal<
            MGMatrixScaleRotation
        >
    >
)