package good.damn.engine2.models

import good.damn.apigl.drawers.GLDrawerFramebufferG
import good.damn.apigl.drawers.GLDrawerLightDirectional
import good.damn.apigl.drawers.GLDrawerVolumes

data class MGMDrawers(
    val drawerFramebuffer: GLDrawerFramebufferG,
    val drawerLightDirectional: GLDrawerLightDirectional,
    val drawerVolumes: GLDrawerVolumes
)