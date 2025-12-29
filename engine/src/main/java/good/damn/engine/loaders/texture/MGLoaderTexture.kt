package good.damn.engine.loaders.texture

import android.graphics.Bitmap
import android.opengl.GLES30
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureBitmap

class MGLoaderTexture
: MGILoaderTexture {

    companion object {
        @JvmField
        val INSTANCE = MGLoaderTexture()
    }

    override fun loadTexture(
        bitmap: Bitmap,
        texture: MGTexture
    ) {
        MGTextureBitmap(
            texture
        ).run {
            texture.generate()
            glTextureSetup(
                bitmap,
                GLES30.GL_REPEAT
            )
        }
    }
}