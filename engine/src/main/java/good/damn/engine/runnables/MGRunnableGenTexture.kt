package good.damn.engine.runnables

import android.graphics.Bitmap
import android.opengl.GLES30
import good.damn.engine.opengl.textures.MGTexture

class MGRunnableGenTexture(
    private val texture: MGTexture,
    private val bitmap: Bitmap
): Runnable {

    override fun run() {
        texture.glTextureSetup(
            bitmap,
            GLES30.GL_REPEAT
        )
    }
}