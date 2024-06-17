package good.damn.opengles_engine.opengl.textures

import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLUtils
import good.damn.opengles_engine.Application

class Texture(
    assetPath: String,
    program: Int,
    wrapMode: Int = GL_CLAMP_TO_EDGE
) {

    private var mId = intArrayOf(
        1
    )

    private val mUniformTexture = glGetUniformLocation(
        program,
        "texture"
    )

    init {

        val inp = Application.ASSETS.open(
            assetPath
        )

        val bitmap = BitmapFactory.decodeStream(
            inp
        )

        glGenTextures(
            1,
            mId,
            0
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

        glTexParameterf(
            GL_TEXTURE_2D,
            GL_MAX_TEXTURE_LOD_BIAS,
            -0.4f
        )

    }

    fun draw() {

        glActiveTexture(
            GL_TEXTURE0
        )

        glBindTexture(
            GL_TEXTURE_2D,
            mId[0]
        )

        glUniform1i(
            mUniformTexture,
            0
        )
    }
}