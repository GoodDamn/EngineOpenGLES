package good.damn.apigl.textures

import android.opengl.GLES30.*
import good.damn.apigl.drawers.GLIDrawerTexture
import good.damn.apigl.shaders.GLIShaderTextureUniform

class GLTexture(
    var textureActive: GLTextureActive
): GLIDrawerTexture<GLIShaderTextureUniform> {

    val id: Int
        get() = mId[0]

    private val mId = intArrayOf(1)

    fun generate() {
        glGenTextures(
            1, mId,
            0
        )
    }

    fun delete() {
        glDeleteTextures(
            1, mId,
            0
        )
    }

    fun unbind() {
        glActiveTexture(
            textureActive.activeTexture
        )

        glBindTexture(
            GL_TEXTURE_2D,
            0
        )
    }

    fun bind() {
        glActiveTexture(
            textureActive.activeTexture
        )

        glBindTexture(
            GL_TEXTURE_2D,
            mId[0]
        )
    }

    final override fun unbind(
        shader: GLIShaderTextureUniform
    ) {
        unbind()
        glUniform1i(
            shader.uniformTexture,
            textureActive.textureUniformId
        )
    }

    final override fun draw(
        shader: GLIShaderTextureUniform
    ) {
        bind()
        glUniform1i(
            shader.uniformTexture,
            textureActive.textureUniformId
        )
    }
}