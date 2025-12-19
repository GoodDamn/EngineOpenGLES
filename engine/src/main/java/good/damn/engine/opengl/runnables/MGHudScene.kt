package good.damn.engine.opengl.runnables

import good.damn.engine.hud.MGHud
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.scripts.MGScriptLightPlacement
import good.damn.engine.sdk.SDVector3
import good.damn.engine.utils.MGUtilsFile
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MGHudScene(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator,
    framebufferG: MGFramebuffer
) {
    val hud: MGHud
    val runnableCycle: MGIRunnableBounds

    init {
        val switcherDrawMode = MGSwitcherDrawMode(
            informator,
            MGDrawerFramebufferG(
                framebufferG
            )
        )

        val scriptLightPlacement = MGScriptLightPlacement(
            MGUtilsFile.getPublicFile(
                "scripts"
            ),
            informator.managerTriggerLight
        )
        scriptLightPlacement.execute()

        hud = MGHud(
            requesterUserContent,
            informator,
            switcherDrawMode
        )

        runnableCycle = MGRunnableCycleSwitcherDrawMode(
            switcherDrawMode
        )
    }


}