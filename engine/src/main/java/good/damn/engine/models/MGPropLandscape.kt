package good.damn.engine.models

import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.shaders.MGShaderOpaque
import java.util.LinkedList

data class MGPropLandscape(
    val materialTexture: MGMaterialTexture,
    val shaderOpaque: MGShaderOpaque,
    val textures: Array<MGMLandscapeTexture>
)