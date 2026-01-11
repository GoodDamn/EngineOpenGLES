package good.damn.wrapper.hud.ui.clicks

import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.hud.MGIClick

class MGClickPlaceMesh(
    private val bridge: MGBridgeRayIntersect
): good.damn.hud.MGIClick {

    override fun onClick() {
        bridge.intersectUpdate = null
    }

}