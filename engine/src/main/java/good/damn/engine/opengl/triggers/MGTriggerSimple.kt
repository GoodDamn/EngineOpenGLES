package good.damn.engine.opengl.triggers

import android.util.Log
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3D
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices

class MGTriggerSimple(
    private val dirLight: MGDrawerLightDirectional,
    shader: MGIShaderModel,
    modelMatrix: MGMMatrix,
    min: MGVector,
    max: MGVector
): MGTriggerBaseDebug(
    MGTriggerMethodBox(
        min, max
    ),
    MGArrayVertex().apply {
        configure(
            MGUtilsBuffer.createFloat(
                MGUtilsVertIndices.createCubeVertices(
                    min, max
                )
            ),
            MGUtilsBuffer.createInt(
                MGUtilsVertIndices.createCubeIndices()
            ),
            stride = 3 * 4
        )
    },
    shader,
    modelMatrix
) {
    override fun onTriggerBegin() {
        Log.d("TAG", "onTriggerBegin: TRIGGER BEGIN")
        dirLight.ambient = 0.5f
    }

    override fun onTriggerEnd() {
        Log.d("TAG", "onTriggerBegin: TRIGGER END")
        dirLight.ambient = 0.05f
    }
}