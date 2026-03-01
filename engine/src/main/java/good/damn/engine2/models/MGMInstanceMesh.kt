package good.damn.engine2.models

import good.damn.apigl.arrays.GLArrayVertexInstanced
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.shaders.GLShaderGeometryPassInstanced
import good.damn.engine.sdk.matrices.SDMatrixScaleRotation
import good.damn.engine.sdk.matrices.SDMatrixTransformationNormal

data class MGMInstanceMesh(
    val shader: GLShaderGeometryPassInstanced,
    val vertexArray: GLArrayVertexInstanced,
    val material: Array<GLMaterial>,
    val enableCullFace: Boolean,
    val matrices: List<
        SDMatrixTransformationNormal<
            SDMatrixScaleRotation
            >
    >
)