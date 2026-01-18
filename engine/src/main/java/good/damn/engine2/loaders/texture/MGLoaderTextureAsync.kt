package good.damn.engine2.loaders.texture

import android.graphics.Bitmap
import good.damn.apigl.runnables.MGRunglTextureSetupBitmap
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
            MGRunglTextureSetupBitmap(
                GLTextureBitmap(
                    texture
                ),
                bitmap
            )
        )
    }
}