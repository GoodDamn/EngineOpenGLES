package good.damn.engine.opengl.drawers.volume

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint

class MGVolumeLight(
    private val drawerLightPoint: MGDrawerLightPoint,
    override val drawerModel: MGDrawerPositionEntity
): MGIVolume {

    override fun isOnFrustrum(
        v: Boolean
    ) {
        drawerLightPoint.isActive = v
    }

}