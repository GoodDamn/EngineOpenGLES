package good.damn.engine2.drawmodes

import good.damn.apigl.GLRenderVars
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.engine2.providers.MGProviderGL

abstract class MGDrawModeBase(
    val lightPassDrawer: GLDrawerLightPass,
    private val drawModeMesh: GLEnumDrawModeMesh
): MGProviderGL(), MGIDrawMode {
    override fun applyDrawMode() {
        GLRenderVars.drawModeMesh = drawModeMesh
    }
}