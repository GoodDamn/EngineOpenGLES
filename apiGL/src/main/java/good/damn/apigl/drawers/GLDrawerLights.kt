package good.damn.apigl.drawers

import android.opengl.GLES30
import android.util.SparseArray
import androidx.core.util.forEach
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLShaderLightPoint
import good.damn.apigl.shaders.GLShaderTexture
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.apigl.textures.GLTexture
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue

class GLDrawerLights(
    private val drawerLightPass: GLDrawerLightPass
) {
    val lights = LinkedList<GLDrawerLightPoint>()

    fun draw(
        shader: GLShaderLightPoint,
        shaderModel: GLIShaderModel,
        textures: Array<GLShaderTexture>
    ) {
        lights.forEach {
            GLDrawerLightPoint.draw(
                shader,
                shaderModel,
                it
            )
            drawerLightPass.draw(
                textures
            )
        }
    }
}
