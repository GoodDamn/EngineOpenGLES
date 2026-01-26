package good.damn.wrapper.hud

import good.damn.common.COHandlerGl
import good.damn.common.COIRunnableBounds
import good.damn.common.camera.COICameraFree
import good.damn.common.utils.COUtilsFile
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMManagers
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.opengl.MGMGeometry
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.engine2.opengl.MGSwitcherDrawMode
import good.damn.engine2.opengl.MGRunnableCycleSwitcherDrawMode
import good.damn.engine2.opengl.pools.MGMPools
import good.damn.script.SCScriptLightPlacement

class APHudScene(
    switcherDrawMode: MGSwitcherDrawMode,
    requesterUserContent: APIRequestUserContent,
    camera: COICameraFree,
    managers: MGMManagers,
    parameters: MGMParameters,
    pools: MGMPools,
    shaders: MGMInformatorShader,
    geometry: MGMGeometry,
    glHandler: COHandlerGl
) {
    val hud: APHud
    val runnableCycle: COIRunnableBounds

    init {
        val scriptLightPlacement = SCScriptLightPlacement(
            COUtilsFile.getPublicFile(
                "scripts"
            ),
            managers.managerProcessTime,
            managers.managerLight,
            managers.managerFrustrum
        )
        scriptLightPlacement.execute()

        hud = APHud(
            camera,
            requesterUserContent,
            switcherDrawMode,
            parameters,
            pools,
            shaders,
            managers,
            geometry,
            glHandler
        )

        runnableCycle = MGRunnableCycleSwitcherDrawMode(
            switcherDrawMode
        )
    }


}