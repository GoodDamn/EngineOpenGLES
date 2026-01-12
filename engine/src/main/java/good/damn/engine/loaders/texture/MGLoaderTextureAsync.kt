package good.damn.engine.loaders.texture

import android.graphics.Bitmap
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureBitmap
import good.damn.common.MGHandlerGl
import good.damn.engine.runnables.MGRunnableTextureSetupBitmap

class MGLoaderTextureAsync(
    private val glHandler: good.damn.common.MGHandlerGl
): MGILoaderTexture {

    override fun loadTexture(
        bitmap: Bitmap,
        texture: MGTexture
    ) {
        glHandler.post(
            MGRunnableTextureSetupBitmap(
                MGTextureBitmap(
                    texture
                ),
                bitmap
            )
        )
    }
}