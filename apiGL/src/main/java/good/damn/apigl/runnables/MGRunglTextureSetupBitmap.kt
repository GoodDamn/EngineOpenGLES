package good.damn.apigl.runnables

import android.graphics.Bitmap
import android.opengl.GLES30
import good.damn.apigl.textures.GLTextureBitmap
import good.damn.common.COIRunnableBounds

class MGRunglTextureSetupBitmap(
    private val texture: GLTextureBitmap,
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