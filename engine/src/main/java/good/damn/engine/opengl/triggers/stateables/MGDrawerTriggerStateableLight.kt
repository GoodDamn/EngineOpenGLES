package good.damn.engine.opengl.triggers.stateables

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerState

class MGDrawerTriggerStateableLight(
    val light: SDMLightPoint,
    val stateManager: MGManagerTriggerState,
    val modelMatrix: MGMatrixTriggerLight
): MGIDrawerShader<MGIShaderModel> {
    private val mEntity = MGDrawerPositionEntity(
        modelMatrix.matrixTrigger.model
    )

    override fun draw(
        shader: MGIShaderModel
    ) {
        mEntity.draw(
            shader
        )
    }
}