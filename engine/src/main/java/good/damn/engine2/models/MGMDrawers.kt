package good.damn.engine2.models

import good.damn.apigl.drawers.GLDrawerFramebufferG
import good.damn.apigl.drawers.GLDrawerLightDirectional

data class MGMDrawers(
    val drawerFramebuffer: GLDrawerFramebufferG,
    val drawerLightDirectional: GLDrawerLightDirectional
)