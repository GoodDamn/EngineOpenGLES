package good.damn.engine.opengl.drawers.material

import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerMaterialTextureNormal(
    override var texture: MGTexture
): MGIDrawerMaterialTexture {

    override fun draw(
        shader: MGShaderMaterial
    ) {
        texture.draw(
            shader.textureNormal
        )
    }

    override fun unbind(
        shader: MGShaderMaterial
    ) {
        texture.unbind(
            shader.textureNormal
        )
    }

}