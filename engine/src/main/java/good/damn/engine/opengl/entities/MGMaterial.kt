package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture

class MGMaterial(
    var textureDiffuse: MGTexture,
    var textureMetallic: MGTexture,
    var textureEmissive: MGTexture
): MGIDrawerTexture<MGShaderMaterial> {
    var shine = 1f

    companion object {
        fun createWithPath(
            poolTextures: MGPoolTextures,
            textureNameDiffuse: String?,
            textureNameMetallic: String?,
            textureNameEmissive: String?
        ) = MGMaterial(
            loadTextureCached(
                poolTextures,
                MGEnumTextureType.DIFFUSE,
                textureNameDiffuse,
                poolTextures.defaultTexture
            ),
            loadTextureCached(
                poolTextures,
                MGEnumTextureType.METALLIC,
                textureNameMetallic,
                poolTextures.defaultTextureMetallic
            ),
            loadTextureCached(
                poolTextures,
                MGEnumTextureType.EMISSIVE,
                textureNameEmissive,
                poolTextures.defaultTextureEmissive
            )
        )

        private fun loadTextureCached(
            poolTextures: MGPoolTextures,
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
                    textureType
                )

                poolTextures.add(
                    textureName,
                    texture
                )
            } catch (_: Exception) { }

            return texture ?: defaultTexture
        }
    }

    override fun draw(
        shader: MGShaderMaterial
    ) {
        GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )

        textureDiffuse.draw(
            shader.textureDiffuse
        )
        textureMetallic.draw(
            shader.textureMetallic
        )
        textureEmissive.draw(
            shader.textureEmissive
        )
    }

    override fun unbind(
        shader: MGShaderMaterial
    ) {
        textureDiffuse.unbind(
            shader.textureDiffuse
        )
        textureMetallic.unbind(
            shader.textureMetallic
        )
        textureEmissive.unbind(
            shader.textureEmissive
        )
    }

}