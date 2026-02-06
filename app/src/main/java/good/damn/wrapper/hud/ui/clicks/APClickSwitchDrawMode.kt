package good.damn.wrapper.hud.ui.clicks

import good.damn.common.COHandlerGl
import good.damn.common.COIRunnableBounds
import good.damn.engine2.opengl.MGSwitcherDrawMode
import good.damn.hud.UIIClick
import good.damn.wrapper.providers.APProviderGL

class APClickSwitchDrawMode(
    private val switcher: MGSwitcherDrawMode,
): APProviderGL(),
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