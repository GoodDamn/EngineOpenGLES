package good.damn.wrapper.hud.ui.clicks

import good.damn.hud.UIIClick
import good.damn.engine2.providers.MGProviderGL

class APClickTriggerDrawingFlag
: MGProviderGL(), UIIClick {
    override fun onClick() {
        glProvider.parameters.apply {
            canDrawTriggers = !canDrawTriggers
        }
    }

}