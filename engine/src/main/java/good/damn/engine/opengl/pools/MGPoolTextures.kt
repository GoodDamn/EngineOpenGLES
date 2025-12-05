package good.damn.engine.opengl.pools

import android.opengl.GLES30.GL_CLAMP_TO_EDGE
import android.opengl.GLES30.GL_REPEAT
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBitmap

class MGPoolTextures {
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