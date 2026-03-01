package good.damn.apigl.drawers

import good.damn.engine.sdk.matrices.SDMatrixModel
import good.damn.common.volume.COIVolume

class GLVolumeLight(
    private val drawerLightPoint: GLDrawerLightPoint,
    override val modelMatrix: SDMatrixModel
): COIVolume {

    override fun isOnFrustrum(
        v: Boolean
    ) {
        drawerLightPoint.isActive = v
    }

}