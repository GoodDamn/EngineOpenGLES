package good.damn.apigl.textures

import good.damn.apigl.drawers.GLDrawerTextureActive
import good.damn.apigl.enums.GLEnumTextureType

data class GLMTexturePart(
    val textureType: GLEnumTextureType,
    val drawer: GLDrawerTextureActive,
    val textureName: String
)