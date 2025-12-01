package good.damn.engine.opengl.pools

import android.opengl.GLES30.GL_CLAMP_TO_EDGE
import android.opengl.GLES30.GL_REPEAT
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBitmap

class MGPoolTextures(
    val defaultTexture: MGTexture,
    val defaultTextureMetallic: MGTexture,
    val defaultTextureEmissive: MGTexture,
    val defaultTextureOpacity: MGTexture,
    val defaultTextureNormal: MGTexture
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

    fun configureDefault() {
        MGUtilsBitmap.generateFromColor(
            0
        ).run {
            defaultTextureMetallic.glTextureSetup(
                this,
                GL_REPEAT
            )

            defaultTextureEmissive.glTextureSetup(
                this,
                GL_REPEAT
            )

            defaultTextureNormal.glTextureSetup(
                this,
                GL_REPEAT
            )
        }

        MGUtilsBitmap.generateFromColor(
            -1
        ).run {
            defaultTexture.glTextureSetup(
                this,
                GL_REPEAT
            )

            defaultTextureOpacity.glTextureSetup(
                this,
                GL_REPEAT
            )
        }

        MGUtilsBitmap.generateFromColor(
            0xff7f7fff.toInt()
        ).run {
            defaultTextureNormal.glTextureSetup(
                this,
                GL_CLAMP_TO_EDGE
            )
        }
    }
}