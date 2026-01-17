package good.damn.engine.opengl.models

import good.damn.apigl.shaders.lightpass.MGShaderLightPass

data class MGMLightPass(
    val shader: good.damn.apigl.shaders.lightpass.MGShaderLightPass,
    val vertPath: String,
    val fragPath: String
)