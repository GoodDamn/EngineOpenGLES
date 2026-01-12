package good.damn.wrapper.hud.ui.clicks

import good.damn.common.COIRunnableBounds
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.hud.UIIClick

class MGClickSwitchDrawMode(
    private val informator: MGMInformator,
    private val switcher: MGSwitcherDrawMode,
): UIIClick,
COIRunnableBounds {

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