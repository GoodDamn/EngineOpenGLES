package good.damn.apigl.builders

import good.damn.apigl.drawers.GLDrawerTextureActive
import good.damn.apigl.enums.GLEnumTextureType
import good.damn.apigl.textures.GLMTexturePart
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureActive
import java.util.LinkedList

class GLBuilderMaterialTexture {
    companion object {
        private val DEFAULT = GLTexture(
            GLTextureActive.default
        )
    }

    private val map = LinkedList<
        GLMTexturePart
    >()

    fun buildTexture(
        textureName: String,
        type: GLEnumTextureType,
        activeTexture: GLTextureActive
    ) = apply {
        map.add(
            GLMTexturePart(
                type,
                GLDrawerTextureActive(
                    DEFAULT,
                    activeTexture
                ),
                textureName
            )
        )
    }

    fun build() = map
}