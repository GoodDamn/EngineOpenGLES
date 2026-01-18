package good.damn.engine.opengl.pools

import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureActive
import good.damn.engine.loaders.texture.MGILoaderTexture
import good.damn.engine.utils.MGUtilsBitmap

class MGPoolTextures(
    private val loaderTexture: MGILoaderTexture
) {
    private val map = HashMap<
        String,
        GLTexture
    >(127)

    fun remove(
        name: String
    ) {
        map.remove(
            name
        )
    }

    fun loadOrGetFromCache(
        name: String,
        localPathDir: String
    ): GLTexture? {
        get(name)?.run {
            return this
        }

        val bitmap = MGUtilsBitmap.loadBitmap(
            "$localPathDir/$name"
        ) ?: return null

        val texture = GLTexture(
            GLTextureActive(0)
        )

        loaderTexture.loadTexture(
            bitmap,
            texture
        )

        set(
            name,
            texture
        )

        return texture
    }

    private operator fun get(
        name: String
    ) = map[name]


    private operator fun set(
        name: String,
        texture: GLTexture
    ) {
        map[name] = texture
    }
}