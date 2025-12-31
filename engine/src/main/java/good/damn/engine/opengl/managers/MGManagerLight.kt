package good.damn.engine.opengl.managers

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint
import good.damn.engine.opengl.shaders.MGShaderLightPoint
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import java.util.concurrent.ConcurrentLinkedQueue

class MGManagerLight(
    private val drawerVolume: MGDrawerVertexArray
) {
    private val mLights = ConcurrentLinkedQueue<
        MGDrawerLightPoint
    >()

    fun draw(
        shader: MGShaderLightPoint
    ) {
        mLights.forEach {
            it.draw(
                shader
            )
            drawerVolume.draw(
                GLES30.GL_TRIANGLES
            )
        }
    }

    fun register(
        drawer: MGDrawerLightPoint
    ) {
        mLights.add(
            drawer
        )
    }
}
