package good.damn.engine.ui.clicks

import android.opengl.GLES30.GL_NO_ERROR
import android.opengl.GLES30.glGetError
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.models.MGMDrawMode
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.ui.MGIClick

class MGClickSwitchDrawMode(
    private val handler: MGHandlerGl,
    private val switcher: MGSwitcherDrawMode,
    private val modeOpaque: MGMDrawMode,
    private val modeWireframe: MGMDrawMode,
    private val modeNormals: MGMDrawMode,
    private val modeTexCoords: MGMDrawMode
): MGIClick,
Runnable {

    override fun onClick() {
        handler.post(
            this
        )
    }

    override fun run() {
        when (MGEngine.drawMode) {
            MGEnumDrawMode.OPAQUE -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.OPAQUE,
                    modeWireframe
                )
                MGEngine.drawMode = MGEnumDrawMode.WIREFRAME
            }

            MGEnumDrawMode.WIREFRAME -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.WIREFRAME,
                    modeNormals
                )
                MGEngine.drawMode = MGEnumDrawMode.NORMALS
            }

            MGEnumDrawMode.NORMALS -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.NORMALS,
                    modeTexCoords
                )
                MGEngine.drawMode = MGEnumDrawMode.TEX_COORDS
            }

            MGEnumDrawMode.TEX_COORDS -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.TEX_COORDS,
                    modeOpaque
                )
                MGEngine.drawMode = MGEnumDrawMode.OPAQUE
            }
        }
        val error = glGetError()
        if (error != GL_NO_ERROR) {
            Log.d("TAG", "post: ERROR: ${error.toString(16)}")
        }
    }
}