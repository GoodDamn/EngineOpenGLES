package good.damn.apigl.textures

import android.opengl.GLES30.GL_TEXTURE0

class GLTextureActive(
    val textureUniformId: Int
) {
    companion object {
        @JvmStatic
        val default = GLTextureActive(0)
    }
    val activeTexture: Int = GL_TEXTURE0 + textureUniformId
}