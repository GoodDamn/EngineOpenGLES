package good.damn.wrapper.hud.ui.clicks

import good.damn.engine.models.MGMInformator
import good.damn.hud.MGIClick

class MGClickTriggerDrawingFlag(
    private val informator: MGMInformator
): good.damn.hud.MGIClick {

    override fun onClick() {
        informator.run {
            canDrawTriggers = !canDrawTriggers
        }
    }

}