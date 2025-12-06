package good.damn.engine.opengl.textures

import android.opengl.GLES30.GL_LINEAR
import android.opengl.GLES30.GL_RGB
import android.opengl.GLES30.GL_TEXTURE_2D
import android.opengl.GLES30.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES30.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES30.GL_UNSIGNED_BYTE
import android.opengl.GLES30.glTexImage2D
import android.opengl.GLES30.glTexParameteri

class MGTextureAttachment(
    val texture: MGTexture
) {
    fun glTextureSetup(
        width: Int,
        height: Int
    ) {
        texture.bind()
        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MIN_FILTER,
            GL_LINEAR
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MAG_FILTER,
            GL_LINEAR
        )

        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGB,
            width,
            height,
            0,
            GL_RGB,
            GL_UNSIGNED_BYTE,
            null
        )
        texture.unbind()
    }
}