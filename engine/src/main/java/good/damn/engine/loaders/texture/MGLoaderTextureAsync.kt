package good.damn.engine.loaders.texture

import android.graphics.Bitmap
import good.damn.common.COHandlerGl
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureBitmap
import good.damn.engine.opengl.runnables.misc.MGRunglTextureSetupBitmap

class MGLoaderTextureAsync(
    private val glHandler: COHandlerGl
): MGILoaderTexture {

    override fun loadTexture(
        bitmap: Bitmap,
        texture: MGTexture
    ) {
        glHandler.post(
            MGRunglTextureSetupBitmap(
                MGTextureBitmap(
                    texture
                ),
                bitmap
            )
        )
    }
}