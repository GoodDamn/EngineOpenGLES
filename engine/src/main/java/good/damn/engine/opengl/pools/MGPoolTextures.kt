package good.damn.engine.opengl.pools

import good.damn.engine.loaders.texture.MGILoaderTexture
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive
import good.damn.engine.utils.MGUtilsBitmap

class MGPoolTextures(
    private val loaderTexture: MGILoaderTexture
) {
    private val map = HashMap<
        String,
        MGTexture
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
    ): MGTexture? {
        get(name)?.run {
            return this
        }

        val bitmap = MGUtilsBitmap.loadBitmap(
            "$localPathDir/$name"
        ) ?: return null

        val texture = MGTexture(
            MGTextureActive(0)
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
        texture: MGTexture
    ) {
        map[name] = texture
    }
}