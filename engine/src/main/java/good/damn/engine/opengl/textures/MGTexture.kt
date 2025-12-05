package good.damn.engine.opengl.textures

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLES32
import android.opengl.GLUtils
import android.util.Log
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform
import good.damn.engine.utils.MGUtilsFile
import java.io.FileInputStream

class MGTexture(
    type: MGEnumTextureType
): MGIDrawerTexture<MGIShaderTextureUniform> {

    private var mId = intArrayOf(1)

    private val mActiveTexture = type.v

    fun glTextureSetup(
        bitmap: Bitmap,
        wrapMode: Int = GL_CLAMP_TO_EDGE
    ) {
        glGenTextures(
            1,
            mId,
            0
        )

        glActiveTexture(
            GL_TEXTURE0 + mActiveTexture
        )

        glBindTexture(
            GL_TEXTURE_2D,
            mId[0]
        )

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

        /*glTexParameterf(
            GL_TEXTURE_2D,
            GL_MAX_TEXTURE_LOD_BIAS,
            -1f
        )*/

        val error = glGetError()
        if (error != GL_NO_ERROR) {
            Log.d("MGTexture", "setupTexture: ERROR: ${error.toString(16)}")
        }
        glBindTexture(
            GL_TEXTURE_2D,
            0
        )
    }

    override fun unbind(
        shader: MGIShaderTextureUniform
    ) {
        glActiveTexture(
            GL_TEXTURE0 + mActiveTexture
        )

        glBindTexture(
            GL_TEXTURE_2D,
            0
        )

        glUniform1i(
            shader.uniformTexture,
            mActiveTexture
        )
    }

    override fun draw(
        shader: MGIShaderTextureUniform
    ) {
        glActiveTexture(
            GL_TEXTURE0 + mActiveTexture
        )

        glBindTexture(
            GL_TEXTURE_2D,
            mId[0]
        )

        glUniform1i(
            shader.uniformTexture,
            mActiveTexture
        )
    }
}