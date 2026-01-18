package good.damn.engine.shader.generators

import good.damn.apigl.drawers.GLMaterialTexture
import good.damn.apigl.enums.GLEnumTextureType
import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.textures.GLTextureActive
import good.damn.common.utils.COUtilsFile
import good.damn.engine.opengl.models.MGMShaderSourceFragDefer
import good.damn.engine.shader.MGShaderSource
import java.util.LinkedList

class MGMMaterialShader private constructor(
    val srcCodeMaterial: String,
    val shaderTextures: Array<GLShaderTexture>,
    val materialTexture: GLMaterialTexture
) {
    companion object {
        private var default: MGMMaterialShader? = null

        @JvmStatic
        fun getDefault(
            shaderSource: MGShaderSource
        ): MGMMaterialShader {
            default?.apply {
                return this
            }
            default = Builder(
                "",
                "",
                shaderSource
            ).diffuse()
                .opacity()
                .emissive(1.0f)
                .useDepthFunc()
                .normal()
                .specular().build()
            return default!!
        }
    }

    class Builder(
        private val textureRootName: String,
        private val localPathDir: String,
        private val shaderSource: MGShaderSource
    ) {
        private val mGeneratorMaterial = MGGeneratorMaterialG(
            shaderSource
        )

        private val mShaderTextures = LinkedList<GLShaderTexture>()
        private val mBuilder = GLMaterialTexture.Builder()

        private var mCurrentIndex = 0

        fun diffuse() = apply {
            componeEntity(
                shaderSource.fragDeferDiffuse,
                GLEnumTextureType.DIFFUSE
            )
        }

        fun useDepthFunc() = apply {
            mGeneratorMaterial.componeEntity(
                shaderSource.fragDeferDepth.fragDefer
            )
        }

        fun useDepthConstant() = apply {
            mGeneratorMaterial.componeEntity(
                shaderSource.fragDeferDepth.fragDeferNo
            )
        }

        fun specular() = apply {
            componeEntity(
                shaderSource.fragDeferSpecular,
                GLEnumTextureType.METALLIC
            )
        }

        fun emissive(
            defaultValue: Float
        ) = apply {
            componeEntity(
                shaderSource.fragDeferEmissive,
                GLEnumTextureType.EMISSIVE,
                defaultValue
            )
        }

        fun opacity() = apply {
            componeEntity(
                shaderSource.fragDeferOpacity,
                GLEnumTextureType.OPACITY
            )
        }

        fun normal() = apply {
            componeEntity(
                shaderSource.fragDeferNormal,
                GLEnumTextureType.NORMAL
            )
        }

        fun build() = MGMMaterialShader(
            mGeneratorMaterial.build(),
            mShaderTextures.toTypedArray(),
            mBuilder.build()
        )

        private fun componeEntity(
            fragDeferTexture: MGMShaderSourceFragDefer,
            textureType: GLEnumTextureType
        ) {
            componeEntity(
                fragDeferTexture,
                textureType,
                0.0f
            )
        }

        private fun componeEntity(
            fragDeferTexture: MGMShaderSourceFragDefer,
            textureType: GLEnumTextureType,
            defaultValue: Float
        ) {
            val fileName = "${textureRootName}${fragDeferTexture.extension}"
            if (textureExists(fileName)) {
                componeTexture(
                    fragDeferTexture,
                    fileName,
                    textureType,
                )
                return
            }

            mGeneratorMaterial.componeEntity(
                fragDeferTexture.fragDeferNo.replace(
                    "$0",
                    defaultValue.toString()
                )
            )
        }

        private inline fun componeTexture(
            fragDeferTexture: MGMShaderSourceFragDefer,
            fileName: String,
            textureType: GLEnumTextureType,
        ) {
            mGeneratorMaterial.componeEntity(
                fragDeferTexture.fragDefer
            )

            mBuilder.buildTexture(
                fileName,
                textureType,
                GLTextureActive(
                    mCurrentIndex++
                )
            )

            mShaderTextures.add(
                GLShaderTexture(
                    fragDeferTexture.id
                )
            )
        }

        private inline fun textureExists(
            fileName: String
        ) = COUtilsFile.getPublicFile(
            "$localPathDir/$fileName"
        ).exists()
    }
}