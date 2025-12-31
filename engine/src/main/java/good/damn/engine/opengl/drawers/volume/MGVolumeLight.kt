package good.damn.engine.opengl.drawers.volume

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.matrices.MGMatrixModel
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.sdk.models.SDMLightPoint

class MGVolumeLight(
    override val drawerModel: MGDrawerPositionEntity
): MGIVolume {

    override fun onFrustrumState(
        isOnFrustrum: Boolean
    ) {

    }
}