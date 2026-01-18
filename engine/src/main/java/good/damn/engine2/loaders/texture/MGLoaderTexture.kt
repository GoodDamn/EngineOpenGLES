package good.damn.engine2.loaders.texture

import android.graphics.Bitmap
import android.opengl.GLES30
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureBitmap

class MGLoaderTexture
: MGILoaderTexture {

    companion object {
        @JvmField
        val INSTANCE = MGLoaderTexture()
    }

    override fun loadTexture(
        bitmap: Bitmap,
        texture: GLTexture
    ) {
        GLTextureBitmap(
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