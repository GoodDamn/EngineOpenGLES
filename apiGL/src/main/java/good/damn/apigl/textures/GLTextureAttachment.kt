package good.damn.apigl.textures

import android.opengl.GLES30
import android.opengl.GLES30.GL_NEAREST
import android.opengl.GLES30.GL_TEXTURE_2D
import android.opengl.GLES30.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES30.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES30.GL_UNSIGNED_BYTE
import android.opengl.GLES30.glTexImage2D
import android.opengl.GLES30.glTexParameteri

class GLTextureAttachment(
    val attachment: Int,
    val texture: GLTexture
) {
    fun glTextureSetup(
        width: Int,
        height: Int,
        config: GLMConfig
    ) {
        texture.bind()
        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MIN_FILTER,
            GL_NEAREST
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MAG_FILTER,
            GL_NEAREST
        )

        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            config.internalFormat,
            width,
            height,
            0,
            config.outFormat,
            config.type,
            null
        )
        texture.unbind()
    }

    data class GLMConfig(
        val internalFormat: Int,
        val outFormat: Int,
        val type: Int
    ) {
        companion object {
            @JvmField
            val rgba = GLMConfig(
                GLES30.GL_RGBA,
                GLES30.GL_RGBA,
                GL_UNSIGNED_BYTE
            )
        }
    }
}