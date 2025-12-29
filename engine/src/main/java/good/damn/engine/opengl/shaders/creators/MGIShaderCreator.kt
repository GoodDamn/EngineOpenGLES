package good.damn.engine.opengl.shaders.creators

import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.MGShaderBase

interface MGIShaderCreator<
    T: MGShaderBase,
    PARAM
> {
    fun create(
        param: PARAM
    ): T
}