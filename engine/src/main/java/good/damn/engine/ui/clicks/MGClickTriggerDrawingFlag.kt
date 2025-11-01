package good.damn.engine.ui.clicks

import good.damn.engine.opengl.drawers.MGDrawerModeOpaque
import good.damn.engine.ui.MGIClick

class MGClickTriggerDrawingFlag(
    private val drawerModeOpaque: MGDrawerModeOpaque
): MGIClick {

    override fun onClick() {
        drawerModeOpaque.run {
            canDrawTriggers = !canDrawTriggers
        }
    }

}