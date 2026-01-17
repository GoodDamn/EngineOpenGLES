package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLShaderLightPoint
import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.textures.GLTexture
import java.util.concurrent.ConcurrentLinkedQueue

class GLDrawerLights(
    private val drawerVolume: GLDrawerVertexArray
) {
    private val mLights = ConcurrentLinkedQueue<
        GLDrawerLightPoint
    >()

    fun draw(
        shader: GLShaderLightPoint,
        shaderModel: GLIShaderModel,
        shaderTextures: Array<GLShaderTexture>,
        textures: Array<GLTexture>
    ) {
        mLights.forEach {
            it.draw(
                shader,
                shaderModel
            )
            var i = 0
            textures.forEach {
                it.draw(
                    shaderTextures[i]
                )
                i++
            }

            drawerVolume.draw(
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

    fun register(
        drawer: good.damn.apigl.drawers.GLDrawerLightPoint
    ) {
        mLights.add(
            drawer
        )
    }
}
