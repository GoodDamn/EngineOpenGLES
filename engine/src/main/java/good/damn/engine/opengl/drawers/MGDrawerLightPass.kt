package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.shaders.MGShaderLightPass
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerLightPass(
    private val texturePosition: MGTexture,
    private val textureNormal: MGTexture,
    private val textureColorSpec: MGTexture,
    private val textureMisc: MGTexture,
    private val textureDepth: MGTexture,
    private val quad: MGDrawerVertexArray
) {

    fun draw(
        shader: MGShaderLightPass
    ) {
        texturePosition.draw(
            shader.texturePosition
        )

        textureNormal.draw(
            shader.textureNormal
        )

        textureColorSpec.draw(
            shader.textureColorSpec
        )

        textureMisc.draw(
            shader.textureMisc
        )

        textureDepth.draw(
            shader.textureDepth
        )

        quad.draw(
            GLES30.GL_TRIANGLES
        )

        texturePosition.unbind(
            shader.texturePosition
        )

        textureNormal.unbind(
            shader.textureNormal
        )

        textureColorSpec.unbind(
            shader.textureColorSpec
        )

        textureMisc.unbind(
            shader.textureMisc
        )

        textureDepth.unbind(
            shader.textureDepth
        )
    }
}