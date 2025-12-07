package good.damn.engine.opengl.runnables

import good.damn.engine.hud.MGHud
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode

class MGHudScene(
    requesterUserContent: MGIRequestUserContent,
    informator: MGMInformator
) {
    val hud: MGHud
    val runnableCycle: MGIRunnableBounds

    init {
        val switcherDrawMode = MGSwitcherDrawMode(
            informator
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