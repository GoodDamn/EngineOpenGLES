package good.damn.engine.models

import good.damn.apigl.textures.MGTextureBitmap

data class MGMLandscapeTexture(
    val diffuse: good.damn.apigl.textures.MGTextureBitmap,
    val control: good.damn.apigl.textures.MGTextureBitmap
)