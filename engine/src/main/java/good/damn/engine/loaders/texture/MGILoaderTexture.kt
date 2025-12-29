package good.damn.engine.loaders.texture

import android.graphics.Bitmap
import good.damn.engine.opengl.textures.MGTexture

interface MGILoaderTexture {
    fun loadTexture(
        bitmap: Bitmap,
        texture: MGTexture
    )
}