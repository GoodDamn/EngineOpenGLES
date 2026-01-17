package good.damn.engine.opengl

import good.damn.common.COIRunnableBounds

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