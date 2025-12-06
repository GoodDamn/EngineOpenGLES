package good.damn.engine.runnables

import android.graphics.Bitmap
import android.opengl.GLES30
import good.damn.engine.opengl.textures.MGTextureBitmap

class MGRunnableTextureSetupBitmap(
    private val texture: MGTextureBitmap,
    private val bitmap: Bitmap
): Runnable {

    override fun run() {
        texture.texture.generate()
        texture.glTextureSetup(
            bitmap,
            GLES30.GL_REPEAT
        )
    }
}