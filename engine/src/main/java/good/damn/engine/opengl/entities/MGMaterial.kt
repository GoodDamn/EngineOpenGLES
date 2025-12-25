package good.damn.engine.opengl.entities

import android.util.Log
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.models.MGMShaderMaterialModel
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderMaterial.Companion.singleMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGMaterialShader

class MGMaterial(
    private val materialTexture: MGMaterialTexture
): MGIDrawerTexture<MGShaderMaterial> {

    companion object {
        @JvmStatic
        fun generateShaderAndMaterial(
            fileNameDiffuse: String?,
            informator: MGMInformator
        ) = "textures".run {
            val materialShader = if (
                fileNameDiffuse == null
            ) MGMaterialShader.getDefault(
                informator.shaders.source
            ) else MGMaterialShader.Builder(
                fileNameDiffuse,
                this,
                informator.shaders.source
            ).diffuse()
                .opacity()
                .emissive(0.0f)
                .normal()
                .specular()
                .useDepthFunc()
                .build()

            return@run generateShaderAndMaterial(
                materialShader,
                informator,
                this
            )
        }

        @JvmStatic
        fun generateShaderAndMaterial(
            materialShader: MGMaterialShader,
            informator: MGMInformator,
            localDirPath: String
        ): MGMShaderMaterialModel {
            val glHandler = informator.glHandler
            val loaderTexture = informator.glLoaderTexture
            val shaders = informator.shaders
            val shaderCache = shaders.cacheGeometryPass

            val src = materialShader.srcCodeMaterial

            var cachedShader = shaderCache[src]
            if (cachedShader == null) {
                cachedShader = MGShaderGeometryPassModel(
                    singleMaterial(
                        materialShader.shaderTextures.toTypedArray()
                    )
                )
                shaderCache.cacheAndCompile(
                    src,
                    shaders.source.vert,
                    cachedShader,
                    glHandler,
                    MGBinderAttribute.Builder()
                        .bindPosition()
                        .bindTextureCoordinates()
                        .bindNormal()
                        .build()
                )
            }

            val materialTexture = materialShader.materialTexture

            materialTexture.load(
                informator.poolTextures,
                localDirPath,
                loaderTexture
            )

            val material = MGMaterial(
                materialTexture
            )

            return MGMShaderMaterialModel(
                cachedShader,
                arrayOf(
                    material
                )
            )
        }
    }

    var shine = 1f

    fun getTextureByType(
        type: MGEnumTextureType
    ) = materialTexture.getTextureByType(
        type
    )

    override fun draw(
        shader: MGShaderMaterial
    ) {
        /*GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )*/

        materialTexture.draw(
            shader.textures
        )
    }

    override fun unbind(
        shader: MGShaderMaterial
    ) {
        materialTexture.unbind(
            shader.textures
        )
    }

}