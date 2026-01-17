package good.damn.wrapper.hud

import good.damn.common.COIRunnableBounds
import good.damn.common.utils.COUtilsFile
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.runnables.MGRunnableCycleSwitcherDrawMode
import good.damn.engine.utils.MGUtilsFile
import good.damn.script.SCScriptLightPlacement

class APHudScene(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator,
    framebufferGG: MGFrameBufferG
) {
    val hud: APHud
    val runnableCycle: COIRunnableBounds

    init {
        val switcherDrawMode = MGSwitcherDrawMode(
            informator,
            MGDrawerFramebufferG(
                framebufferGG
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