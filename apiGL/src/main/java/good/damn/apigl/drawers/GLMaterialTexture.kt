package good.damn.apigl.drawers

import good.damn.apigl.enums.GLEnumTextureType
import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureActive
import java.util.LinkedList

class GLMaterialTexture private constructor(
    private val list: List<MGMTexturePart>
): GLIDrawerTexture<
    Array<GLShaderTexture>
> {
    override fun draw(
        shader: Array<GLShaderTexture>
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
        shader: Array<GLShaderTexture>
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
        localPath: String
    ) {
        list.forEach {
            poolTextures.loadOrGetFromCache(
                it.textureName,
                localPath
            )?.run {
                it.drawer.texture = this
            }
        }
    }

    class Builder {
        companion object {
            private val DEFAULT = GLTexture(
                GLTextureActive.default
            )
        }

        private val map = LinkedList<
            MGMTexturePart
        >()

        fun buildTexture(
            textureName: String,
            type: GLEnumTextureType,
            activeTexture: GLTextureActive
        ) = apply {
            map.add(
                MGMTexturePart(
                    type,
                    GLDrawerMaterialTexture(
                        DEFAULT,
                        activeTexture
                    ),
                    textureName
                )
            )
        }

        fun build() = GLMaterialTexture(
            map
        )
    }

    private data class MGMTexturePart(
        val textureType: GLEnumTextureType,
        val drawer: GLDrawerMaterialTexture,
        val textureName: String
    )
}