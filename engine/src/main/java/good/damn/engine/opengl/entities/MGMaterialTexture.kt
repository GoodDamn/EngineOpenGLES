package good.damn.engine.opengl.entities

import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.drawers.material.MGDrawerMaterialTextureDiffuse
import good.damn.engine.opengl.drawers.material.MGDrawerMaterialTextureEmissive
import good.damn.engine.opengl.drawers.material.MGDrawerMaterialTextureMetallic
import good.damn.engine.opengl.drawers.material.MGDrawerMaterialTextureNormal
import good.damn.engine.opengl.drawers.material.MGDrawerMaterialTextureOpacity
import good.damn.engine.opengl.drawers.material.MGIDrawerMaterialTexture
import good.damn.engine.opengl.entities.MGMaterialTexture.MGMTexturePart
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableGenTexture
import good.damn.engine.utils.MGUtilsBitmap
import java.util.LinkedList

class MGMaterialTexture private constructor(
    private val list: Array<MGMTexturePart>
): MGIDrawerTexture<MGShaderMaterial> {

    override fun draw(
        shader: MGShaderMaterial
    ) {
        list.forEach {
            it.drawer.draw(shader)
        }
    }

    override fun unbind(
        shader: MGShaderMaterial
    ) {
        list.forEach {
            it.drawer.unbind(shader)
        }
    }

    fun getTextureByType(
        type: MGEnumTextureType
    ) = list[
        type.v
    ].drawer.texture

    fun load(
        poolTextures: MGPoolTextures,
        localPath: String,
        glHandler: MGHandlerGl
    ) {
        list.forEach {
            loadTextureDrawerCached(
                it,
                localPath,
                poolTextures,
                glHandler
            )
        }
    }

    private fun loadTextureDrawerCached(
        part: MGMTexturePart,
        localPath: String,
        poolTextures: MGPoolTextures,
        glHandler: MGHandlerGl
    ) {
        val textureName = part.textureName
            ?: return

        var texture = poolTextures.get(
            textureName
        )

        if (texture != null) {
            part.drawer.texture = texture
            return
        }

        val bitmap = MGUtilsBitmap.loadBitmap(
            "$localPath/$textureName"
        ) ?: return

        texture = MGTexture(
            part.type
        )

        glHandler.post(
            MGRunnableGenTexture(
                texture,
                bitmap
            )
        )

        poolTextures.add(
            textureName,
            texture
        )

        part.drawer.texture = texture
    }

    class Builder(
        poolTextures: MGPoolTextures
    ) {
        private val mTextureDiffuse = MGMTexturePart(
            MGEnumTextureType.DIFFUSE,
            MGDrawerMaterialTextureDiffuse(
                poolTextures.defaultTexture
            )
        )

        private val mTextureMetallic = MGMTexturePart(
            MGEnumTextureType.METALLIC,
            MGDrawerMaterialTextureMetallic(
                poolTextures.defaultTextureMetallic
            )
        )

        private val mTextureEmissive = MGMTexturePart(
            MGEnumTextureType.EMISSIVE,
            MGDrawerMaterialTextureEmissive(
                poolTextures.defaultTextureEmissive
            )
        )

        private val mTextureOpacity = MGMTexturePart(
            MGEnumTextureType.OPACITY,
            MGDrawerMaterialTextureOpacity(
                poolTextures.defaultTextureOpacity
            )
        )

        private val mTextureNormal = MGMTexturePart(
            MGEnumTextureType.NORMAL,
            MGDrawerMaterialTextureNormal(
                poolTextures.defaultTextureNormal
            )
        )

        fun textureDiffuse(
            textureName: String
        ): Builder {
            mTextureDiffuse.textureName = textureName
            return this
        }

        fun textureMetallic(
            textureName: String
        ): Builder {
            mTextureMetallic.textureName = textureName
            return this
        }

        fun textureEmissive(
            textureName: String
        ): Builder {
            mTextureEmissive.textureName = textureName
            return this
        }

        fun textureOpacity(
            textureName: String
        ): Builder {
            mTextureOpacity.textureName = textureName
            return this
        }

        fun textureNormal(
            textureName: String
        ): Builder {
            mTextureNormal.textureName = textureName
            return this
        }

        fun build() = MGMaterialTexture(
            arrayOf(
                mTextureDiffuse,
                mTextureMetallic,
                mTextureEmissive,
                mTextureOpacity,
                mTextureNormal,
            )
        )
    }

    private data class MGMTexturePart(
        val type: MGEnumTextureType,
        val drawer: MGIDrawerMaterialTexture,
        var textureName: String? = null
    )
}