package good.damn.wrapper.hud.ui.clicks

import good.damn.common.COIRunnableBounds
import good.damn.engine2.opengl.MGSwitcherDrawMode
import good.damn.hud.UIIClick
import good.damn.engine2.providers.MGProviderGL

class APClickSwitchDrawMode(
    private val switcher: MGSwitcherDrawMode,
): MGProviderGL(),
UIIClick,
COIRunnableBounds {

    override fun onClick() {
        glProvider.glHandler.post(
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