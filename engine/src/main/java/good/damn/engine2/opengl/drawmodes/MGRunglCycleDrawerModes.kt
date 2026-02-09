package good.damn.engine2.opengl.drawmodes

import good.damn.common.COIRunnableBounds
import good.damn.engine2.providers.MGIProviderGLRegister
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.providers.MGProviderGL

class MGRunglCycleDrawerModes(
    private val drawModes: Array<MGDrawModeBase>
): MGIProviderGLRegister, COIRunnableBounds {
    private var mCurrentDrawModeIndex = 0

    override fun run(
        width: Int,
        height: Int
    ) {
        drawModes[
            mCurrentDrawModeIndex
        ].draw(
            width,
            height
        )
    }

    override fun registerGlProvider(
        provider: MGMProviderGL
    ) {
        drawModes.forEach {
            it.glProvider = provider
        }
    }

    fun switchDrawMode() {
        mCurrentDrawModeIndex++
        if (mCurrentDrawModeIndex >= drawModes.size) {
            mCurrentDrawModeIndex = 0
        }

        drawModes[
            mCurrentDrawModeIndex
        ].applyDrawMode()
    }

}