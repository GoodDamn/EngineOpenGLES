package good.damn.wrapper.hud.ui.clicks

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.runnables.MGIRunnableBounds
import good.damn.hud.MGIClick

class MGClickSwitchDrawMode(
    private val informator: MGMInformator,
    private val switcher: MGSwitcherDrawMode,
): good.damn.hud.MGIClick,
MGIRunnableBounds {

    override fun onClick() {
        informator.glHandler.post(
            this
        )
    }

    override fun run(
        width: Int,
        height: Int
    ) {
        switcher.switchDrawMode()
    }
}