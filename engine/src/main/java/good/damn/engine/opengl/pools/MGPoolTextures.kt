package good.damn.engine.opengl.pools

import good.damn.engine.opengl.textures.MGTexture

class MGPoolTextures(
    val defaultTexture: MGTexture,
    val defaultTextureMetallic: MGTexture,
    val defaultTextureEmissive: MGTexture,
    val defaultTextureOpacity: MGTexture
) {
    private val map = HashMap<
        String,
        MGTexture
    >(127)

    fun get(
        name: String
    ) = map[name]

    fun add(
        name: String,
        texture: MGTexture
    ) {
        map[name] = texture
    }

    fun remove(
        name: String
    ) {
        map.remove(
            name
        )
    }
}