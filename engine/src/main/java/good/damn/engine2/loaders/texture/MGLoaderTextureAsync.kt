package good.damn.engine2.loaders.texture

import android.graphics.Bitmap
import good.damn.apigl.runnables.GLRunglTextureSetupBitmap
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureBitmap
import good.damn.common.COHandlerGl

class MGLoaderTextureAsync(
    private val glHandler: COHandlerGl
): MGILoaderTexture {

    override fun loadTexture(
        bitmap: Bitmap,
        texture: GLTexture
    ) {
        glHandler.post(
            GLRunglTextureSetupBitmap(
                GLTextureBitmap(
                    texture
                ),
                bitmap
            )
        )
    }
}