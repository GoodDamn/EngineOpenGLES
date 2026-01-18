package good.damn.engine.models

import good.damn.apigl.arrays.GLArrayVertexInstanced
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.shaders.GLShaderGeometryPassInstanced
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal

data class MGMInstanceMesh(
    val shader: GLShaderGeometryPassInstanced,
    val vertexArray: GLArrayVertexInstanced,
    val material: Array<GLMaterial>,
    val enableCullFace: Boolean,
    val matrices: List<
        COMatrixTransformationNormal<
            COMatrixScaleRotation
            >
    >
)