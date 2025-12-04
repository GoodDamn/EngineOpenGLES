package good.damn.engine.opengl.entities

import android.util.Log
import android.util.SparseArray
import androidx.core.util.forEach
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.drawers.material.MGDrawerMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableGenTexture
import good.damn.engine.utils.MGUtilsBitmap

class MGMaterialTexture private constructor(
    private val list: SparseArray<MGMTexturePart>
): MGIDrawerTexture<
    Array<MGShaderTexture>
> {
    override fun draw(
        shader: Array<MGShaderTexture>
    ) {
        var i = 0
        list.forEach { _, it ->
            it.drawer.draw(
                shader[i]
            )
            i++
        }
    }

    override fun unbind(
        shader: Array<MGShaderTexture>
    ) {
        var i = 0
        list.forEach { _, it ->
            it.drawer.unbind(
                shader[i]
            )
            i++
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
        list.forEach { _, it ->
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

    class Builder {
        companion object {
            private val DEFAULT = MGTexture(
                MGEnumTextureType.DIFFUSE
            )
        }

        private val map = SparseArray<
            MGMTexturePart
        >()

        fun textureDiffuse(
            textureName: String
        ): Builder {
            map.put(
                MGEnumTextureType.DIFFUSE.v,
                MGMTexturePart(
                    MGEnumTextureType.DIFFUSE,
                    MGDrawerMaterialTexture(
                        DEFAULT
                    ),
                    textureName
                )
            )
            return this
        }

        fun textureMetallic(
            textureName: String
        ): Builder {
            map.put(
                MGEnumTextureType.METALLIC.v,
                MGMTexturePart(
                    MGEnumTextureType.METALLIC,
                    MGDrawerMaterialTexture(
                        DEFAULT
                    ),
                    textureName
                )
            )
            return this
        }

        fun textureEmissive(
            textureName: String
        ): Builder {
            map.put(
                MGEnumTextureType.EMISSIVE.v,
                MGMTexturePart(
                    MGEnumTextureType.EMISSIVE,
                    MGDrawerMaterialTexture(
                        DEFAULT
                    ),
                    textureName
                )
            )
            return this
        }

        fun textureOpacity(
            textureName: String
        ): Builder {
            map.put(
                MGEnumTextureType.OPACITY.v,
                MGMTexturePart(
                    MGEnumTextureType.OPACITY,
                    MGDrawerMaterialTexture(
                        DEFAULT
                    ),
                    textureName
                )
            )
            return this
        }

        fun textureNormal(
            textureName: String
        ): Builder {
            map.put(
                MGEnumTextureType.NORMAL.v,
                MGMTexturePart(
                    MGEnumTextureType.NORMAL,
                    MGDrawerMaterialTexture(
                        DEFAULT
                    ),
                    textureName
                )
            )
            return this
        }

        fun build() = MGMaterialTexture(
            map
        )
    }

    private data class MGMTexturePart(
        val type: MGEnumTextureType,
        val drawer: MGDrawerMaterialTexture,
        val textureName: String
    )
}