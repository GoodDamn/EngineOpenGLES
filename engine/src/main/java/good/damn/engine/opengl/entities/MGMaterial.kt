package good.damn.engine.opengl.entities

import android.graphics.BitmapFactory
import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableGenTexture
import good.damn.engine.utils.MGUtilsBitmap
import good.damn.engine.utils.MGUtilsFile
import java.io.FileInputStream

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
            textureNameEmissive: String?,
            localPath: String,
            handler: MGHandlerGl
        ) = MGMaterial(
            loadTextureCached(
                poolTextures,
                MGEnumTextureType.DIFFUSE,
                textureNameDiffuse,
                poolTextures.defaultTexture,
                localPath,
                handler
            ),
            loadTextureCached(
                poolTextures,
                MGEnumTextureType.METALLIC,
                textureNameMetallic,
                poolTextures.defaultTextureMetallic,
                localPath,
                handler
            ),
            loadTextureCached(
                poolTextures,
                MGEnumTextureType.EMISSIVE,
                textureNameEmissive,
                poolTextures.defaultTextureEmissive,
                localPath,
                handler
            )
        )

        private fun loadTextureCached(
            poolTextures: MGPoolTextures,
            textureType: MGEnumTextureType,
            textureName: String?,
            defaultTexture: MGTexture,
            localPath: String,
            handler: MGHandlerGl
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

            val bitmap = MGUtilsBitmap.loadBitmap(
                "$localPath/$textureName"
            ) ?: return defaultTexture

            texture = MGTexture(
                textureType
            )

            handler.post(
                MGRunnableGenTexture(
                    texture,
                    bitmap
                )
            )

            poolTextures.add(
                textureName,
                texture
            )

            return texture
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