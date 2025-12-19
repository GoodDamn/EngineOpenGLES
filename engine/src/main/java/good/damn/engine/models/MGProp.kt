package good.damn.engine.models

import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import java.util.LinkedList

data class MGProp(
    val fileNameA3d: String,
    val materialTexture: Array<MGMaterialTexture>,
    val shaderOpaque: MGShaderGeometryPassInstanced,
    val enableCullFace: Boolean,
    val matrices: LinkedList<
        MGMatrixTransformationNormal<
            MGMatrixScaleRotation
            >
        >
)