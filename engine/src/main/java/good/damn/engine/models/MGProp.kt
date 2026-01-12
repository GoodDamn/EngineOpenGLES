package good.damn.engine.models

import good.damn.engine.opengl.entities.MGMaterial
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import java.util.LinkedList

data class MGProp(
    val fileNameA3d: String,
    val materials: Array<MGMaterial>,
    val shaderOpaque: MGShaderGeometryPassInstanced,
    val enableCullFace: Boolean,
    val matrices: LinkedList<
        COMatrixTransformationNormal<
            COMatrixScaleRotation
            >
        >
)