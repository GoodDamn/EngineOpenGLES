package good.damn.engine2.models

import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.shaders.GLShaderGeometryPassInstanced
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import java.util.LinkedList

data class MGProp(
    val fileNameA3d: String,
    val materials: Array<GLMaterial>,
    val shaderOpaque: GLShaderGeometryPassInstanced,
    val enableCullFace: Boolean,
    val matrices: LinkedList<
        COMatrixTransformationNormal<
            COMatrixScaleRotation
            >
        >
)