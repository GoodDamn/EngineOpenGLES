package good.damn.engine.opengl.triggers

import android.opengl.GLES30
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.utils.MGUtilsBuffer

abstract class MGTriggerBaseDebug(
    triggerMethod: MGITriggerMethod,
    vertices: FloatArray,
    indices: IntArray
): MGTriggerBase(
    triggerMethod
), MGIDrawer {
    private val vertexArray = MGArrayVertex().apply {
        configure(
            MGUtilsBuffer.createFloat(
                vertices
            ),
            MGUtilsBuffer.createInt(
                indices
            ),
            stride = 3 * 4
        )
    }

    override fun draw() {
        vertexArray.draw(
            GLES30.GL_LINES
        )
    }
}