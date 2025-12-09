package good.damn.engine.opengl.runnables

import good.damn.engine.hud.MGHud
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer

class MGHudScene(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator
) {
    val hud: MGHud
    val runnableCycle: MGIRunnableBounds

    init {
        val framebuffer = MGFramebuffer()
        val framebufferG = MGFrameBufferG(
            framebuffer
        )

        val switcherDrawMode = MGSwitcherDrawMode(
            informator,
            framebuffer
        )

        informator.glHandler.post(
            object: MGIRunnableBounds {
                override fun run(width: Int, height: Int) {
                    framebufferG.generate(
                        width, height
                    )
                }

            }
        )

        hud = MGHud(
            requesterUserContent,
            informator,
            switcherDrawMode
        )

        runnableCycle = MGRunnableCycleSwitcherDrawMode(
            informator,
            switcherDrawMode
        )
    }

}