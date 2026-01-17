package good.damn.engine.opengl.runnables.misc

import android.graphics.Bitmap
import android.opengl.GLES30
import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.textures.MGTextureBitmap

class MGRunglTextureSetupBitmap(
    private val texture: MGTextureBitmap,
    private val bitmap: Bitmap
): COIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        texture.texture.generate()
        texture.glTextureSetup(
            bitmap,
            GLES30.GL_REPEAT
        )
    }
}