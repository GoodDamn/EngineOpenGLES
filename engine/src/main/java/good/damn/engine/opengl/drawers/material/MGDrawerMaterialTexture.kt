package good.damn.engine.opengl.drawers.material

import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive

class MGDrawerMaterialTexture(
    var texture: MGTexture,
    val activeTexture: MGTextureActive
): MGIDrawerTexture<MGShaderTexture> {

    override fun draw(
        shader: MGShaderTexture
    ) {
        texture.textureActive = activeTexture
        texture.draw(
            shader
        )
    }

    override fun unbind(
        shader: MGShaderTexture
    ) {
        texture.textureActive = activeTexture
        texture.unbind(
            shader
        )
    }

}