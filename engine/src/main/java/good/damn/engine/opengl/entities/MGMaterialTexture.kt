package good.damn.engine.opengl.entities

import android.graphics.Bitmap
import android.util.SparseArray
import androidx.core.util.forEach
import good.damn.engine.loaders.texture.MGILoaderTexture
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.drawers.material.MGDrawerMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive
import good.damn.engine.opengl.textures.MGTextureBitmap
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableTextureSetupBitmap
import good.damn.engine.utils.MGUtilsBitmap
import java.util.LinkedList

class MGMaterialTexture private constructor(
    private val list: List<MGMTexturePart>
): MGIDrawerTexture<
    Array<MGShaderTexture>
> {
    companion object {
        @JvmStatic
        fun loadTextureDrawerCached(
            textureActive: MGTextureActive,
            textureName: String,
            localPath: String,
            poolTextures: MGPoolTextures,
            textureLoader: MGILoaderTexture
        ): MGTexture? {
            var texture = poolTextures.get(
                textureName
            )

            if (texture != null) {
                texture.textureActive = textureActive
                return texture
            }

            val bitmap = MGUtilsBitmap.loadBitmap(
                "$localPath/$textureName"
            ) ?: return null

            texture = MGTexture(
                textureActive
            )

            textureLoader.loadTexture(
                bitmap,
                texture
            )

            poolTextures.add(
                textureName,
                texture
            )

            return texture
        }
    }

    override fun draw(
        shader: Array<MGShaderTexture>
    ) {
        var i = 0
        list.forEach {
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
        list.forEach {
            it.drawer.unbind(
                shader[i]
            )
            i++
        }
    }

    fun load(
        poolTextures: MGPoolTextures,
        localPath: String,
        textureLoader: MGILoaderTexture
    ) {
        list.forEach {
            loadTextureDrawerCached(
                it.drawer.activeTexture,
                it.textureName,
                localPath,
                poolTextures,
                textureLoader
            )?.run {
                it.drawer.texture = this
            }
        }
    }

    class Builder {
        companion object {
            private val DEFAULT = MGTexture(
                MGTextureActive.default
            )
        }

        private val map = LinkedList<
            MGMTexturePart
        >()

        fun buildTexture(
            textureName: String,
            type: MGEnumTextureType,
            activeTexture: MGTextureActive
        ) = apply {
            map.add(
                MGMTexturePart(
                    type,
                    MGDrawerMaterialTexture(
                        DEFAULT,
                        activeTexture
                    ),
                    textureName
                )
            )
        }

        fun build() = MGMaterialTexture(
            map
        )
    }

    private data class MGMTexturePart(
        val textureType: MGEnumTextureType,
        val drawer: MGDrawerMaterialTexture,
        val textureName: String
    )
}