package good.damn.engine.opengl.models

import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced

data class MGMShader<
    SINGLE,
    INSTANCED
>(
    val single: SINGLE,
    val instanced: INSTANCED
)