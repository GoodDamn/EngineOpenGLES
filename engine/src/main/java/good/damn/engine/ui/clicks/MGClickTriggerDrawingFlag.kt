package good.damn.engine.ui.clicks

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.modes.MGDrawModeOpaque
import good.damn.engine.ui.MGIClick

class MGClickTriggerDrawingFlag(
    private val informator: MGMInformator
): MGIClick {

    override fun onClick() {
        informator.run {
            canDrawTriggers = !canDrawTriggers
        }
    }

}