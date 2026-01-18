package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.apigl.textures.GLTexture

class GLDrawerLightPass(
    private val textures: Array<GLTexture>,
    private val drawerVertices: GLDrawerVertexArray
) {
    fun draw(
        shaderTextures: Array<GLShaderTexture>
    ) {
        var i = 0
        textures.forEach {
            it.draw(
                shaderTextures[i]
            )
            i++
        }

        drawerVertices.draw(
            GLES30.GL_TRIANGLES
        )

        i = 0
        textures.forEach {
            it.unbind(
                shaderTextures[i]
            )
            i++
        }
    }
}