package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.apigl.textures.GLTexture

class GLDrawerLightPass(
    private val textures: Array<GLTexture>,
    private val quad: GLDrawerVertexArray
) {
    fun draw(
        shader: GLShaderLightPass
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