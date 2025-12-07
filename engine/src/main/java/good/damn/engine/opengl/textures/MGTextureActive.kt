package good.damn.engine.opengl.textures

import android.opengl.GLES30.GL_TEXTURE0
import good.damn.engine.opengl.enums.MGEnumTextureType

class MGTextureActive {
    val textureUniformId: Int
    val activeTexture: Int

    constructor(
        textureUniformId: Int
    ) {
        this.textureUniformId = textureUniformId
        activeTexture = GL_TEXTURE0 + textureUniformId
    }

    constructor(
        type: MGEnumTextureType
    ): this(type.v)
}