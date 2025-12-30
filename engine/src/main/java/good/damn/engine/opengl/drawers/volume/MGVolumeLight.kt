package good.damn.engine.opengl.drawers.volume

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint
import good.damn.engine.opengl.matrices.MGMatrixModel
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.sdk.models.SDMLightPoint

class MGVolumeLight(
    val light: MGDrawerLightPoint,
    override val drawerModel: MGDrawerPositionEntity
): MGIVolume {

    override fun onFrustrumState(
        isOnFrustrum: Boolean
    ) {
        light.isActive = if (
            isOnFrustrum
        ) 1 else 0
    }
}