package good.damn.wrapper.hud

import good.damn.apigl.drawers.GLDrawerFramebufferG
import good.damn.apigl.drawers.GLDrawerLightDirectional
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.common.COIRunnableBounds
import good.damn.common.utils.COUtilsFile
import good.damn.engine.models.MGMInformatorShader
import good.damn.engine.opengl.MGMGeometry
import good.damn.engine.opengl.MGMVolume
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.MGRunnableCycleSwitcherDrawMode
import good.damn.script.SCScriptLightPlacement

class APHudScene(
    requesterUserContent: MGIRequestUserContent,
    framebufferG: GLFrameBufferG,
    shaders: MGMInformatorShader,
    geometry: MGMGeometry,
    volume: MGMVolume,
    drawerLightDirect: GLDrawerLightDirectional
) {
    val hud: APHud
    val runnableCycle: COIRunnableBounds

    init {
        val switcherDrawMode = MGSwitcherDrawMode(
            framebufferG,
            shaders,
            geometry,
            volume,
            drawerLightDirect,
            GLDrawerFramebufferG(
                framebufferG
            )
        )

        val scriptLightPlacement = SCScriptLightPlacement(
            COUtilsFile.getPublicFile(
                "scripts"
            ),
            informator.managerProcessTime,
            informator.managerLight,
            informator.managerLightVolumes
        )
        scriptLightPlacement.execute()

        hud = APHud(
            requesterUserContent,
            informator,
            switcherDrawMode
        )

        runnableCycle = MGRunnableCycleSwitcherDrawMode(
            switcherDrawMode
        )
    }


}