package good.damn.wrapper.models

import good.damn.common.COHandlerGl
import good.damn.common.camera.COICameraFree
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.opengl.MGMGeometry
import good.damn.engine2.opengl.pools.MGMPools

data class APMProviderGL(
    var geometry: MGMGeometry,
    var pools: MGMPools,
    var managers: MGMManagers,
    var shaders: MGMInformatorShader,
    var parameters: MGMParameters,
    var camera: COICameraFree,
    var glHandler: COHandlerGl
)