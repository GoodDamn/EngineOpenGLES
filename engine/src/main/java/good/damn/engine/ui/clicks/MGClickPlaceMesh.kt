package good.damn.engine.ui.clicks

import good.damn.engine.MGEngine
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.ui.MGIClick

class MGClickPlaceMesh(
    private val bridge: MGBridgeRayIntersect
): MGIClick {

    override fun onClick() {
        bridge.intersectUpdate = null
    }

}