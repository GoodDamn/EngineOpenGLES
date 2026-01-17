package good.damn.engine.loaders.texture

import android.graphics.Bitmap
import good.damn.apigl.textures.MGTexture

interface MGILoaderTexture {
    fun loadTexture(
        bitmap: Bitmap,
        texture: good.damn.apigl.textures.MGTexture
    )
}