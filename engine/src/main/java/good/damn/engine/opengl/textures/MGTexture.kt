package good.damn.engine.opengl.textures

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES30.*
import android.opengl.GLES32
import android.opengl.GLUtils
import android.util.Log
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform
import good.damn.engine.utils.MGUtilsFile
import java.io.FileInputStream

class MGTexture
: MGIDrawerTexture<MGIShaderTextureUniform> {

    val id: Int
        get() = mId[0]

    private val mId = intArrayOf(1)

    private val textureUniformId: Int
    private val mActiveTexture: Int

    constructor(
        textureUniformId: Int
    ) {
        this.textureUniformId = textureUniformId
        mActiveTexture = GL_TEXTURE0 + textureUniformId
    }

    constructor(
        type: MGEnumTextureType
    ): this(type.v)

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
            mActiveTexture
        )

        glBindTexture(
            GL_TEXTURE_2D,
            0
        )
    }

    fun bind() {
        glActiveTexture(
            mActiveTexture
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
            textureUniformId
        )
    }

    final override fun draw(
        shader: MGIShaderTextureUniform
    ) {
        bind()
        glUniform1i(
            shader.uniformTexture,
            textureUniformId
        )
    }
}