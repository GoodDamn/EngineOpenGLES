package good.damn.engine.ui.clicks

import android.opengl.GLES30.GL_NO_ERROR
import android.opengl.GLES30.glGetError
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.modes.MGDrawModeOpaque
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleMap
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShader
import good.damn.engine.opengl.drawers.modes.MGDrawModeSingleShaderNormals
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.ui.MGIClick

class MGClickSwitchDrawMode(
    private val informator: MGMInformator,
    private val switcher: MGSwitcherDrawMode,
): MGIClick,
Runnable {

    override fun onClick() {
        informator.glHandler.post(
            this
        )
    }

    override fun run() {
        switcher.switchDrawMode()
    }
}