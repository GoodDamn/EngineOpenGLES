package good.damn.engine2.models

import good.damn.apigl.textures.GLTextureBitmap

data class MGMLandscapeTexture(
    val diffuse: GLTextureBitmap,
    val control: GLTextureBitmap
)