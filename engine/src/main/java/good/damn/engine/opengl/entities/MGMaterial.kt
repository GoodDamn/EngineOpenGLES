package good.damn.engine.opengl.entities

import good.damn.engine.loaders.MGLoaderLevelLibrary
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.models.MGMShaderMaterialModel
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderMaterial.Companion.singleMaterial
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGGeneratorMaterialG
import java.util.LinkedList

class MGMaterial(
    private val materialTexture: MGMaterialTexture
): MGIDrawerTexture<MGShaderMaterial> {

    companion object {
        private inline fun makeTextureFileName(
            fileNameDiffuse: String?,
            extension: String
        ): String? = if (
            fileNameDiffuse == null
        ) null else fileNameDiffuse + extension

        @JvmStatic
        fun generateShaderAndMaterial(
            fileNameDiffuse: String?,
            informator: MGMInformator
        ) = generateShaderAndMaterial(
            fileNameDiffuse,
            informator,
            "textures"
        )

        @JvmStatic
        fun generateShaderAndMaterial(
            fileNameDiffuse: String?,
            informator: MGMInformator,
            localDirPath: String
        ): MGMShaderMaterialModel {
            val glHandler = informator
                .glHandler

            val loaderTexture = informator
                .glLoaderTexture

            val shaders = informator
                .shaders

            val shaderCache = shaders.cacheGeometryPass

            val shaderSource = shaders
                .source

            val builder = MGMaterialTexture.Builder()

            val generatorMaterial = MGGeneratorMaterialG(
                shaderSource
            )

            val shaderTextures = LinkedList<MGShaderTexture>()

            val fileNameMetallic = makeTextureFileName(
                fileNameDiffuse, "_s.jpg"
            )

            val fileNameEmissive = makeTextureFileName(
                fileNameDiffuse, "_e.jpg"
            )

            val fileNameNormal = makeTextureFileName(
                fileNameDiffuse, "_n.jpg"
            )

            val fileNameOpacity = makeTextureFileName(
                fileNameDiffuse, "_o.jpg"
            )

            if (fileNameDiffuse == null) {
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferDiffuseNo
                )
            } else {
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferDiffuse
                )

                builder.buildTexture(
                    fileNameDiffuse,
                    MGEnumTextureType.DIFFUSE
                )

                shaderTextures.add(
                    MGShaderTexture(
                        MGLoaderLevelLibrary.ID_DIFFUSE
                    )
                )
            }

            if (fileNameMetallic == null) {
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferSpecularNo
                )
            } else {
                shaderTextures.add(
                    MGShaderTexture(
                        MGLoaderLevelLibrary.ID_METALLIC
                    )
                )
                builder.buildTexture(
                    fileNameMetallic,
                    MGEnumTextureType.METALLIC
                )
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferSpecular
                )
            }

            if (fileNameOpacity == null) {
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferOpacityNo
                )
            } else {
                shaderTextures.add(
                    MGShaderTexture(
                        MGLoaderLevelLibrary.ID_OPACITY
                    )
                )

                builder.buildTexture(
                    fileNameOpacity,
                    MGEnumTextureType.OPACITY
                )

                generatorMaterial.componeEntity(
                    shaderSource.fragDeferOpacity
                )
            }

            if (fileNameEmissive == null) {
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferEmissiveNo
                )
            } else {
                shaderTextures.add(
                    MGShaderTexture(
                        MGLoaderLevelLibrary.ID_EMISSIVE
                    )
                )
                builder.buildTexture(
                    fileNameEmissive,
                    MGEnumTextureType.EMISSIVE
                )
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferEmissive
                )
            }

            if (fileNameNormal == null) {
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferNormalVertex
                )
            } else {
                shaderTextures.add(
                    MGShaderTexture(
                        MGLoaderLevelLibrary.ID_NORMAL
                    )
                )
                builder.buildTexture(
                    fileNameEmissive!!,
                    MGEnumTextureType.NORMAL
                )
                generatorMaterial.componeEntity(
                    shaderSource.fragDeferNormal
                )
            }

            val src = generatorMaterial.build()

            var cachedShader = shaderCache[src]

            if (cachedShader == null) {
                cachedShader = MGShaderGeometryPassModel(
                    singleMaterial(
                        shaderTextures.toTypedArray<MGShaderTexture>()
                    )
                )
                shaderCache.cacheAndCompile(
                    src,
                    shaderSource.vert,
                    cachedShader,
                    glHandler,
                    MGBinderAttribute.Builder()
                        .bindPosition()
                        .bindTextureCoordinates()
                        .bindNormal()
                        .build()
                )
            }

            val materialTexture = builder
                .build()

            materialTexture.load(
                informator.poolTextures,
                localDirPath,
                loaderTexture
            )

            val material = MGMaterial(
                builder.build()
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