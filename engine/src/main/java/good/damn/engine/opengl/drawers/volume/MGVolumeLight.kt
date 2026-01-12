package good.damn.engine.opengl.drawers.volume

import good.damn.common.volume.COIVolume
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint

class MGVolumeLight(
    private val drawerLightPoint: MGDrawerLightPoint,
    override val drawerModel: MGDrawerPositionEntity
): COIVolume {

    override fun isOnFrustrum(
        v: Boolean
    ) {
        drawerLightPoint.isActive = v
    }

}