package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLShaderLightPoint
import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.apigl.textures.GLTexture
import java.util.concurrent.ConcurrentLinkedQueue

class GLDrawerLights(
    private val drawerLightPass: GLDrawerLightPass
) {
    private val mLights = ConcurrentLinkedQueue<
        GLDrawerLightPoint
    >()

    fun draw(
        shader: GLShaderLightPoint,
        textures: Array<GLShaderTexture>
    ) {
        mLights.forEach {
            it.draw(
                shader
            )
            drawerLightPass.draw(
                textures
            )
        }
    }

    fun register(
        drawer: GLDrawerLightPoint
    ) {
        mLights.add(
            drawer
        )
    }
}
