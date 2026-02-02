package good.damn.apigl.shaders.creators

import good.damn.apigl.shaders.base.GLShaderBase

interface GLIShaderCreator<
    T: GLShaderBase,
    PARAM
> {
    fun create(
        param: PARAM
    ): T
}