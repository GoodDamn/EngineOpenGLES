package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.shaders.MGIShaderTextureUniform

interface MGIDrawerTexture<T>
: MGIDrawerShader<T> {
    fun unbind(
        shader: T
    )
}