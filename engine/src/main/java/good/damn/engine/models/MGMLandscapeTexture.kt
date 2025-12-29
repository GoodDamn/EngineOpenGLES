package good.damn.engine.models

import good.damn.engine.opengl.textures.MGTextureBitmap

data class MGMLandscapeTexture(
    val diffuse: MGTextureBitmap,
    val control: MGTextureBitmap
)