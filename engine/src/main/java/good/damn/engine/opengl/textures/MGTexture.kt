package good.damn.engine.opengl.textures

import android.graphics.BitmapFactory
import android.graphics.PathIterator
import android.opengl.GLES30.*
import android.opengl.GLUtils
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.utils.MGUtilsFile
import java.io.FileInputStream

class MGTexture(
    var shader: MGIShaderTexture
): MGIDrawer {

    private var mId = intArrayOf(1)

    private var mTextureOffset = 1f

    companion object {

        fun getNameAndTextureType(
            textureName: String
        ): Pair<String, MGEnumTextureType> {
            val splitIndex = textureName.indexOf("_")

            if (splitIndex == -1) {
                val dotIndex = textureName.indexOf(".")
                return Pair(
                    if (
                        dotIndex == -1
                    ) textureName else textureName.substring(
                        0,
                        dotIndex
                    ),
                    MGEnumTextureType.DIFFUSE
                )
            }

            if (splitIndex+2 < textureName.length) {
                val textureType = when (
                    textureName.substring(
                        splitIndex+1,
                        splitIndex+2
                    )
                ) {
                    "s" -> MGEnumTextureType.SPECULAR
                    else -> MGEnumTextureType.DIFFUSE
                }

                return Pair(
                    textureName.substring(
                        0,
                        splitIndex
                    ),
                    textureType
                )
            }

            return Pair(
                textureName.substring(
                    0,
                    splitIndex
                ),
                MGEnumTextureType.DIFFUSE
            )
        }

        fun createDefaultAsset(
            fileName: String,
            shader: MGIShaderTexture
        ): MGTexture {
            val texture = MGTexture(
                shader
            )

            texture.setupTexture(
                "textures/$fileName"
            )

            return texture
        }
    }

    fun setupTexture(
        assetPath: String,
        wrapMode: Int = GL_CLAMP_TO_EDGE
    ) {
        val filePub = MGUtilsFile.getPublicFile(
            assetPath
        )

        if (!filePub.exists()) {
            throw Exception("No such file: ${filePub.path}")
        }

        val inp = FileInputStream(
            filePub
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

        /*glGenerateMipmap(
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
        )*/

        val error = glGetError()
        if (error != GL_NO_ERROR) {
            Log.d("MGTexture", "setupTexture: ERROR: ${error.toString(16)}")
        }
    }

    fun unbind() {
        glBindTexture(
            GL_TEXTURE_2D,
            0
        )

        glUniform1i(
            shader.uniformTexture,
            0
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
            shader.uniformTexture,
            0
        )

        glUniform1f(
            shader.uniformTextureOffset,
            mTextureOffset
        )
    }
}