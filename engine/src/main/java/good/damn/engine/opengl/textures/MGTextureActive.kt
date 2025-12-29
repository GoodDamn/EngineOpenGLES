package good.damn.engine.opengl.textures

import android.opengl.GLES30.GL_TEXTURE0
import good.damn.engine.opengl.enums.MGEnumTextureType

class MGTextureActive(
    val textureUniformId: Int
) {
    companion object {
        @JvmStatic
        val default = MGTextureActive(0)
    }
    val activeTexture: Int = GL_TEXTURE0 + textureUniformId
}