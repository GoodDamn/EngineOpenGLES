package good.damn.engine.opengl.models

import good.damn.engine.opengl.shaders.MGShaderLightPass

data class MGMLightPass(
    val shader: MGShaderLightPass,
    val vertPath: String,
    val fragPath: String
)