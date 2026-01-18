package good.damn.wrapper.hud.ui.clicks

import good.damn.engine2.models.MGMParameters
import good.damn.hud.UIIClick

class APClickTriggerDrawingFlag(
    private val parameters: MGMParameters
): UIIClick {

    override fun onClick() {
        parameters.apply {
            canDrawTriggers = !canDrawTriggers
        }
    }

}