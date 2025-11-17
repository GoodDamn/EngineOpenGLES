package good.damn.engine.ui.clicks

import good.damn.engine.opengl.drawers.modes.MGDrawModeOpaque
import good.damn.engine.ui.MGIClick

class MGClickTriggerDrawingFlag(
    private val drawerModeOpaque: MGDrawModeOpaque
): MGIClick {

    override fun onClick() {
        drawerModeOpaque.run {
            canDrawTriggers = !canDrawTriggers
        }
    }

}