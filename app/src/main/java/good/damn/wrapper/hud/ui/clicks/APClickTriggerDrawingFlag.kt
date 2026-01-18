package good.damn.wrapper.hud.ui.clicks

import good.damn.hud.UIIClick

class APClickTriggerDrawingFlag(
    private val informator: MGMInformator
): UIIClick {

    override fun onClick() {
        informator.run {
            canDrawTriggers = !canDrawTriggers
        }
    }

}