package good.damn.wrapper.pools

import good.damn.engine.loaders.texture.MGILoaderTexture
import good.damn.apigl.textures.MGTexture
import good.damn.apigl.textures.MGTextureActive
import good.damn.engine.utils.MGUtilsBitmap


class MGPoolTextures(
    private val loaderTexture: MGILoaderTexture
) {
    private val map = HashMap<
        String,
        good.damn.apigl.textures.MGTexture
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
    ): good.damn.apigl.textures.MGTexture? {
        get(name)?.run {
            return this
        }

        val bitmap = MGUtilsBitmap.loadBitmap(
            "$localPathDir/$name"
        ) ?: return null

        val texture = good.damn.apigl.textures.MGTexture(
            good.damn.apigl.textures.MGTextureActive(0)
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
        texture: good.damn.apigl.textures.MGTexture
    ) {
        map[name] = texture
    }
}