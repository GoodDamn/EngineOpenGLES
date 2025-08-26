package good.damn.engine.opengl.models

import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGIShaderNormal

data class MGMDrawMode(
    val drawer: MGIDrawer,
    val shaderDefault: MGIShaderCamera,
    val shaderSky: MGIShaderCamera,
    val shaderNormals: MGIShaderNormal? = null,
    val shaderNormalsSky: MGIShaderNormal? = null
)