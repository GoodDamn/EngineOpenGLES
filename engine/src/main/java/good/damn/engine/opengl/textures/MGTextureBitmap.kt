package good.damn.engine.opengl.textures

import android.graphics.Bitmap
import android.opengl.GLES30.GL_CLAMP_TO_EDGE
import android.opengl.GLES30.GL_LINEAR
import android.opengl.GLES30.GL_LINEAR_MIPMAP_LINEAR
import android.opengl.GLES30.GL_MAX_TEXTURE_LOD_BIAS
import android.opengl.GLES30.GL_NEAREST
import android.opengl.GLES30.GL_NO_ERROR
import android.opengl.GLES30.GL_TEXTURE_2D
import android.opengl.GLES30.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES30.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES30.GL_TEXTURE_WRAP_S
import android.opengl.GLES30.GL_TEXTURE_WRAP_T
import android.opengl.GLES30.glGenerateMipmap
import android.opengl.GLES30.glGetError
import android.opengl.GLES30.glTexParameterf
import android.opengl.GLES30.glTexParameteri
import android.opengl.GLUtils
import android.util.Log
import good.damn.engine.opengl.enums.MGEnumTextureType

class MGTextureBitmap(
    val texture: MGTexture
) {
    fun glTextureSetup(
        bitmap: Bitmap,
        wrapMode: Int = GL_CLAMP_TO_EDGE
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
            GL_LINEAR
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_WRAP_S,
            wrapMode
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_WRAP_T,
            wrapMode
        )

        GLUtils.texImage2D(
            GL_TEXTURE_2D,
            0,
            bitmap,
            0
        )

        glGenerateMipmap(
            GL_TEXTURE_2D
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MIN_FILTER,
            GL_LINEAR_MIPMAP_LINEAR
        )

        glTexParameterf(
            GL_TEXTURE_2D,
            GL_MAX_TEXTURE_LOD_BIAS,
            -1f
        )

        val error = glGetError()
        if (error != GL_NO_ERROR) {
            Log.d("MGTexture", "setupTexture: ERROR: ${error.toString(16)}")
        }
        texture.unbind()
    }
}