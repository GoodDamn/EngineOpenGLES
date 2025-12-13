package good.damn.engine.opengl.runnables

import good.damn.engine.hud.MGHud
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.sdk.SDVector3
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
            framebufferG
        )
        val lightCount = 8
        val dpi = (PI * 2 / lightCount).toFloat()
        val radius = 1600f
        var current = 0f
        for (i in 0 until lightCount) {
            val light = SDMLightPoint(
                SDVector3(
                    1.0f,
                    .784f,
                    .515f
                ),
                0.6f,
                0.4f,
                0.25f,
                2700f
            )
            MGTriggerLight.createFromLight(
                light
            ).run {
                matrix.radius = 10000f
                matrix.setPosition(
                    250f + radius * sin(current),
                    2160.65f,
                    250f + radius * cos(current)
                )
                matrix.invalidatePosition()
                matrix.invalidateRadius()
                matrix.calculateInvertTrigger()

                informator.managerTriggerLight.addTrigger(
                    triggerState
                )
                current += dpi
            }
        }

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