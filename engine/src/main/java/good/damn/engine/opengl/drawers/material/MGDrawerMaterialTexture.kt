package good.damn.engine.opengl.drawers.material

import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerMaterialTexture(
    var texture: MGTexture
): MGIDrawerTexture<MGShaderTexture> {

    override fun draw(
        shader: MGShaderTexture
    ) {
        texture.draw(
            shader
        )
    }

    override fun unbind(
        shader: MGShaderTexture
    ) {
        texture.unbind(
            shader
        )
    }

}