package good.damn.engine.opengl.triggers

import android.opengl.GLES30
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.utils.MGUtilsBuffer

abstract class MGTriggerBaseDebug(
    triggerMethod: MGITriggerMethod,
    vertexArray: MGArrayVertex,
    shader: MGIShaderModel,
    modelMatrix: MGMMatrix,
    transformedMatrix: MGMMatrix,
): MGTriggerBase(
    triggerMethod,
    modelMatrix,
    transformedMatrix
), MGIDrawer {
    private val mEntity = MGDrawerPositionEntity(
        MGDrawerVertexArray(
            vertexArray
        ),
        shader,
        modelMatrix
    )

    override fun draw() {
        mEntity.draw()
    }
}