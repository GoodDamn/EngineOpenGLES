package good.damn.engine.opengl.models

import good.damn.apigl.shaders.lightpass.GLShaderLightPass

data class MGMLightPass(
    val shader: GLShaderLightPass,
    val vertPath: String,
    val fragPath: String
)