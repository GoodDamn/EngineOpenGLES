package good.damn.engine.opengl.runnables

import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.MGSwitcherDrawMode

class MGRunnableCycleSwitcherDrawMode(
    private val switcherDrawMode: MGSwitcherDrawMode
): COIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        switcherDrawMode
            .currentDrawerMode
            .draw(
                width, height
            )
    }
}