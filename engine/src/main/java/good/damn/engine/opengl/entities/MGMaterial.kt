package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture

class MGMaterial(
    var shader: MGShaderMaterial,
    var textureDiffuse: MGTexture,
    var textureMetallic: MGTexture,
    var textureEmissive: MGTexture
): MGIDrawer {
    var shine = 1f

    companion object {
        fun createWithPath(
            shader: MGShaderMaterial,
            poolTextures: MGPoolTextures,
            textureNameDiffuse: String?,
            textureNameMetallic: String?,
            textureNameEmissive: String?
        ) = MGMaterial(
            shader,
            loadTextureCached(
                poolTextures,
                shader.textureDiffuse,
                MGEnumTextureType.DIFFUSE,
                textureNameDiffuse,
                poolTextures.defaultTexture
            ),
            loadTextureCached(
                poolTextures,
                shader.textureMetallic,
                MGEnumTextureType.METALLIC,
                textureNameMetallic,
                poolTextures.defaultTextureMetallic
            ),
            loadTextureCached(
                poolTextures,
                shader.textureEmissive,
                MGEnumTextureType.EMISSIVE,
                textureNameEmissive,
                poolTextures.defaultTextureEmissive
            )
        )

        private fun loadTextureCached(
            poolTextures: MGPoolTextures,
            shaderTextures: MGIShaderTexture,
            textureType: MGEnumTextureType,
            textureName: String?,
            defaultTexture: MGTexture
        ): MGTexture {
            if (textureName == null) {
                return defaultTexture
            }

            var texture = poolTextures.get(
                textureName
            )

            if (texture != null) {
                return texture
            }

            try {
                texture = MGTexture.createDefaultAsset(
                    textureName,
                    textureType,
                    shaderTextures
                )

                poolTextures.add(
                    textureName,
                    texture
                )
            } catch (_: Exception) { }

            return texture ?: defaultTexture
        }
    }

    override fun draw() {
        GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )

        textureDiffuse.draw()
        textureMetallic.draw()
        textureEmissive.draw()
    }

    fun unbind() {
        textureDiffuse.unbind()
        textureMetallic.unbind()
        textureEmissive.unbind()
    }

}