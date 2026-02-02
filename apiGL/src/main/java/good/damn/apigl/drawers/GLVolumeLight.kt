package good.damn.apigl.drawers

import good.damn.common.matrices.COMatrixModel
import good.damn.common.volume.COIVolume

class GLVolumeLight(
    private val drawerLightPoint: GLDrawerLightPoint,
    override val modelMatrix: COMatrixModel
): COIVolume {

    override fun isOnFrustrum(
        v: Boolean
    ) {
        drawerLightPoint.isActive = v
    }

}