package good.damn.engine.opengl.managers

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.shaders.MGIShaderModel
import java.util.concurrent.ConcurrentLinkedQueue

class MGManagerVolume(
    private val drawerPrimitive: MGDrawerVertexArray
): MGIDrawerShader<MGIShaderModel> {
    private val mVolumes = ConcurrentLinkedQueue<
        MGIDrawerShader<MGIShaderModel>
    >()

    override fun draw(
        shader: MGIShaderModel
    ) {
        mVolumes.forEach {
            it.draw(shader)
            drawerPrimitive.draw(
                GLES30.GL_LINES
            )
        }
    }

    fun addVolume(
        drawer: MGIDrawerShader<MGIShaderModel>
    ) = mVolumes.add(
        drawer
    )
}