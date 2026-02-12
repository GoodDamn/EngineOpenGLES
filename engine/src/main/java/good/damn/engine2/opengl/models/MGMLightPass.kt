package good.damn.engine2.opengl.models

import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.apigl.textures.GLTexture

data class MGMLightPass(
    val inputTextures: Array<GLTexture>,
    val shader: GLShaderLightPass
)