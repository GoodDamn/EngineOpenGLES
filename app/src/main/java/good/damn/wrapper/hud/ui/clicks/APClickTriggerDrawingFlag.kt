package good.damn.wrapper.hud.ui.clicks

import good.damn.engine2.models.MGMParameters
import good.damn.hud.UIIClick
import good.damn.wrapper.providers.APProviderGL

class APClickTriggerDrawingFlag
: APProviderGL(), UIIClick {
    override fun onClick() {
        glProvider.parameters.apply {
            canDrawTriggers = !canDrawTriggers
        }
    }

}