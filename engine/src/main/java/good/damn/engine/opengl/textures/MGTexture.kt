package good.damn.engine.opengl.textures

import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLUtils
import good.damn.engine.MGEngine
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform

class MGTexture
: MGIDrawer,
MGIUniform {

    private var mId = intArrayOf(1)

    private var mUniformTexture = 0
    private var mUniformTextureOffset = 0

    private var mTextureOffset = 1f

    fun setupTexture(
        assetPath: String,
        wrapMode: Int = GL_CLAMP_TO_EDGE
    ) {
        val inp = MGEngine.ASSETS.open(
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

    override fun setupUniforms(
        program: Int
    ) {
        mUniformTexture = glGetUniformLocation(
            program,
            "texture"
        )

        mUniformTextureOffset = glGetUniformLocation(
            program,
            "textureOffset"
        )
    }

    override fun draw() {
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

        glUniform1f(
            mUniformTextureOffset,
            mTextureOffset
        )
    }
}