package good.damn.apigl.drawers

import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureActive

class GLDrawerMaterialTexture(
    var texture: GLTexture,
    val activeTexture: GLTextureActive
): GLIDrawerTexture<GLShaderTexture> {

    override fun draw(
        shader: GLShaderTexture
    ) {
        texture.textureActive = activeTexture
        texture.draw(
            shader
        )
    }

    override fun unbind(
        shader: GLShaderTexture
    ) {
        texture.textureActive = activeTexture
        texture.unbind(
            shader
        )
    }

}