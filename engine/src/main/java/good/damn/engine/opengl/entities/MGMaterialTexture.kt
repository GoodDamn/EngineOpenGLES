package good.damn.engine.opengl.entities

import android.util.SparseArray
import androidx.collection.SparseArrayCompat
import androidx.core.util.forEach
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
    private val list: SparseArray<MGMTexturePart>
): MGIDrawerTexture<MGShaderMaterial> {

    override fun draw(
        shader: MGShaderMaterial
    ) {
        list.forEach { _, value ->
            value.drawer.draw(shader)
        }
    }

    override fun unbind(
        shader: MGShaderMaterial
    ) {
        list.forEach { _, value ->
            value.drawer.unbind(shader)
        }
    }

    fun getTextureByType(
        type: MGEnumTextureType
    ) = list[
        type.v
    ]?.drawer?.texture

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

    class Builder(
        private val poolTextures: MGPoolTextures
    ) {
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
                    MGDrawerMaterialTextureNormal(
                        poolTextures.defaultTexture
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
                    MGDrawerMaterialTextureNormal(
                        poolTextures.defaultTextureMetallic
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
                    MGDrawerMaterialTextureNormal(
                        poolTextures.defaultTextureEmissive
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
                    MGDrawerMaterialTextureNormal(
                        poolTextures.defaultTextureOpacity
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
                    MGDrawerMaterialTextureNormal(
                        poolTextures.defaultTextureNormal
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
        val drawer: MGIDrawerMaterialTexture,
        val textureName: String
    )
}