package good.damn.engine.models

import good.damn.apigl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import good.damn.apigl.shaders.MGShaderGeometryPassInstanced

data class MGMInstanceMesh(
    val shader: good.damn.apigl.shaders.MGShaderGeometryPassInstanced,
    val vertexArray: good.damn.apigl.arrays.MGArrayVertexInstanced,
    val material: Array<MGMaterial>,
    val enableCullFace: Boolean,
    val matrices: List<
        COMatrixTransformationNormal<
            COMatrixScaleRotation
            >
    >
)