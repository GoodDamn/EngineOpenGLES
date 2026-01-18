package good.damn.engine2.loaders.texture

import android.graphics.Bitmap
import good.damn.apigl.textures.GLTexture

interface MGILoaderTexture {
    fun loadTexture(
        bitmap: Bitmap,
        texture: GLTexture
    )
}