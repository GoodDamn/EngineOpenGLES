package good.damn.engine.loaders.texture

import android.graphics.Bitmap
import good.damn.common.COHandlerGl
import good.damn.apigl.textures.MGTexture
import good.damn.apigl.textures.MGTextureBitmap
import good.damn.apigl.runnables.MGRunglTextureSetupBitmap

class MGLoaderTextureAsync(
    private val glHandler: COHandlerGl
): MGILoaderTexture {

    override fun loadTexture(
        bitmap: Bitmap,
        texture: good.damn.apigl.textures.MGTexture
    ) {
        glHandler.post(
            good.damn.apigl.runnables.MGRunglTextureSetupBitmap(
                good.damn.apigl.textures.MGTextureBitmap(
                    texture
                ),
                bitmap
            )
        )
    }
}