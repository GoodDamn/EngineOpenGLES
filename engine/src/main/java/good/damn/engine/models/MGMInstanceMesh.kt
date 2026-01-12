package good.damn.engine.models

import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced

data class MGMInstanceMesh(
    val shader: MGShaderGeometryPassInstanced,
    val vertexArray: MGArrayVertexInstanced,
    val material: Array<MGMaterial>,
    val enableCullFace: Boolean,
    val matrices: List<
        COMatrixTransformationNormal<
            COMatrixScaleRotation
            >
    >
)