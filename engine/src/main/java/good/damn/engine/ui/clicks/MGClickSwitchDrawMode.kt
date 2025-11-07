package good.damn.engine.ui.clicks

import android.opengl.GLES30.GL_NO_ERROR
import android.opengl.GLES30.glGetError
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.ui.MGIClick

class MGClickSwitchDrawMode(
    private val handler: MGHandlerGl,
    private val switcher: MGSwitcherDrawMode,
    private val drawerModeOpaque: MGIDrawer,
    private val drawerModeWireframe: MGIDrawer,
    private val drawerModeNormals: MGIDrawer,
    private val drawerModeTexCoords: MGIDrawer,
    private val drawerModeTexture: MGIDrawer
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
                    MGEnumDrawMode.WIREFRAME,
                    drawerModeWireframe
                )
                MGEngine.drawMode = MGEnumDrawMode.WIREFRAME
            }

            MGEnumDrawMode.WIREFRAME -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.NORMALS,
                    drawerModeNormals
                )
                MGEngine.drawMode = MGEnumDrawMode.NORMALS
            }

            MGEnumDrawMode.NORMALS -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.TEX_COORDS,
                    drawerModeTexCoords
                )
                MGEngine.drawMode = MGEnumDrawMode.TEX_COORDS
            }

            MGEnumDrawMode.TEX_COORDS -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.DIFFUSE,
                    drawerModeTexture
                )
                MGEngine.drawMode = MGEnumDrawMode.DIFFUSE
            }

            MGEnumDrawMode.DIFFUSE -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.METALLIC,
                    drawerModeTexture
                )
                MGEngine.drawMode = MGEnumDrawMode.METALLIC
            }

            MGEnumDrawMode.METALLIC -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.EMISSIVE,
                    drawerModeTexture
                )

                MGEngine.drawMode = MGEnumDrawMode.EMISSIVE
            }

            MGEnumDrawMode.EMISSIVE -> {
                switcher.switchDrawMode(
                    MGEnumDrawMode.OPAQUE,
                    drawerModeOpaque
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