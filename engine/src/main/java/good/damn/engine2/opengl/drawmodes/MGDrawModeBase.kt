package good.damn.engine2.opengl.drawmodes

import good.damn.apigl.GLRenderVars
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.engine2.providers.MGProviderGL

abstract class MGDrawModeBase(
    val lightPassDrawer: GLDrawerLightPass,
    val lightPassShader: GLShaderLightPass
): MGProviderGL(), MGIDrawMode {
    override fun applyDrawMode() {
        GLRenderVars.drawModeMesh = GLEnumDrawModeMesh.TRIANGLES
    }
}