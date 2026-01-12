package good.damn.engine.opengl.textures

import android.opengl.GLES30.*
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform

class MGTexture(
    var textureActive: MGTextureActive
): MGIDrawerTexture<MGIShaderTextureUniform> {

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
        shader: MGIShaderTextureUniform
    ) {
        unbind()
        glUniform1i(
            shader.uniformTexture,
            textureActive.textureUniformId
        )
    }

    final override fun draw(
        shader: MGIShaderTextureUniform
    ) {
        bind()
        glUniform1i(
            shader.uniformTexture,
            textureActive.textureUniformId
        )
    }
}