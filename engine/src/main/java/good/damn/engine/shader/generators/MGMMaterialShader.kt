package good.damn.engine.shader.generators

import android.util.Log
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.models.MGMShaderSourceFragDefer
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive
import good.damn.engine.shader.MGShaderSource
import good.damn.engine.utils.MGUtilsFile
import java.util.LinkedList

class MGMMaterialShader private constructor(
    val srcCodeMaterial: String,
    val shaderTextures: Array<MGShaderTexture>,
    val materialTexture: MGMaterialTexture
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

        private val mShaderTextures = LinkedList<MGShaderTexture>()
        private val mBuilder = MGMaterialTexture.Builder()

        private var mCurrentIndex = 0

        fun diffuse() = apply {
            componeEntity(
                shaderSource.fragDeferDiffuse,
                MGEnumTextureType.DIFFUSE
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
                MGEnumTextureType.METALLIC
            )
        }

        fun emissive(
            defaultValue: Float
        ) = apply {
            componeEntity(
                shaderSource.fragDeferEmissive,
                MGEnumTextureType.EMISSIVE,
                defaultValue
            )
        }

        fun opacity() = apply {
            componeEntity(
                shaderSource.fragDeferOpacity,
                MGEnumTextureType.OPACITY
            )
        }

        fun normal() = apply {
            componeEntity(
                shaderSource.fragDeferNormal,
                MGEnumTextureType.NORMAL
            )
        }

        fun build() = MGMMaterialShader(
            mGeneratorMaterial.build(),
            mShaderTextures.toTypedArray(),
            mBuilder.build()
        )

        private fun componeEntity(
            fragDeferTexture: MGMShaderSourceFragDefer,
            textureType: MGEnumTextureType
        ) {
            componeEntity(
                fragDeferTexture,
                textureType,
                0.0f
            )
        }

        private fun componeEntity(
            fragDeferTexture: MGMShaderSourceFragDefer,
            textureType: MGEnumTextureType,
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
            textureType: MGEnumTextureType,
        ) {
            mGeneratorMaterial.componeEntity(
                fragDeferTexture.fragDefer
            )

            mBuilder.buildTexture(
                fileName,
                textureType,
                MGTextureActive(
                    mCurrentIndex++
                )
            )

            mShaderTextures.add(
                MGShaderTexture(
                    fragDeferTexture.id
                )
            )
        }

        private inline fun textureExists(
            fileName: String
        ) = MGUtilsFile.getPublicFile(
            "$localPathDir/$fileName"
        ).exists()
    }
}