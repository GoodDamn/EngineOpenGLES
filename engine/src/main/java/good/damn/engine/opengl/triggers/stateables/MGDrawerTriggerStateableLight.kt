package good.damn.engine.opengl.triggers.stateables

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.matrices.MGMatrixScale
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.triggers.MGMatrixTriggerLight
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerState
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodSphere
import good.damn.engine.sdk.models.SDMLightPoint

data class MGDrawerTriggerStateableLight(
    val light: SDMLightPoint,
    val stateManager: MGManagerTriggerState,
    val modelMatrix: MGMatrixTriggerLight
) {
    companion object {
        @JvmStatic
        fun createFromLight(
            light: SDMLightPoint
        ): MGDrawerTriggerStateableLight {
            val matrix = MGMatrixTriggerLight(
                MGMatrixTransformationInvert(
                    MGMatrixScale()
                )
            )

            return MGDrawerTriggerStateableLight(
                light,
                MGManagerTriggerState(
                    MGTriggerMethodSphere(
                        matrix.matrixTrigger.invert
                    )
                ),
                matrix
            )
        }
    }
}