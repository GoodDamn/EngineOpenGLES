package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.shaders.MGShaderLightPass
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerLightPass(
    private val textures: Array<MGTexture>,
    private val quad: MGDrawerVertexArray
) {
    fun draw(
        shader: MGShaderLightPass
    ) {
        var i = 0
        textures.forEach {
            it.draw(
                shader.textures[i]
            )
            i++
        }

        quad.draw(
            GLES30.GL_TRIANGLES
        )

        i = 0
        textures.forEach {
            it.unbind(
                shader.textures[i]
            )
            i++
        }
    }
}