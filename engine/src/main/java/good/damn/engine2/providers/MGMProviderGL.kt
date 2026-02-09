package good.damn.engine2.providers

import good.damn.common.COHandlerGl
import good.damn.common.camera.COICameraFree
import good.damn.engine2.models.MGMDrawers
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.opengl.MGMGeometry
import good.damn.engine2.opengl.pools.MGMPools

data class MGMProviderGL(
    val geometry: MGMGeometry,
    val pools: MGMPools,
    val managers: MGMManagers,
    val shaders: MGMInformatorShader,
    val parameters: MGMParameters,
    val camera: COICameraFree,
    val glHandler: COHandlerGl,
    val drawers: MGMDrawers
)