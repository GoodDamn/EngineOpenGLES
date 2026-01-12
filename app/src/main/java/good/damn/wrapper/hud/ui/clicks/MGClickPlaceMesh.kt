package good.damn.wrapper.hud.ui.clicks

import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect
import good.damn.hud.UIIClick

class MGClickPlaceMesh(
    private val bridge: MGBridgeRayIntersect
): UIIClick {

    override fun onClick() {
        bridge.intersectUpdate = null
    }

}