package good.damn.engine.opengl.models

import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPass

data class MGMLightPass(
    val shader: MGShaderLightPass,
    val vertPath: String,
    val fragPath: String
)