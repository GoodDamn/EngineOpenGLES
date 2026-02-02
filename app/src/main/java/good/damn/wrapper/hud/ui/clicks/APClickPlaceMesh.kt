package good.damn.wrapper.hud.ui.clicks

import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.hud.UIIClick

class APClickPlaceMesh(
    private val bridge: APBridgeRayIntersect
): UIIClick {

    override fun onClick() {
        bridge.intersectUpdate = null
    }

}