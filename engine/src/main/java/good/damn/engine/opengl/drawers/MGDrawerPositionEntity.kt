package good.damn.engine.opengl.drawers

import android.opengl.GLES30.*
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.shaders.MGIShader

class MGDrawerPositionEntity(
    private var drawer: MGIDrawer,
    private var shader: MGIShader,
    var modelMatrix: MGMMatrix
): MGIDrawer {

    fun setDrawerShader(
        drawer: MGIDrawer,
        shader: MGIShader
    ) {
        this.shader = shader
        this.drawer = drawer
    }

    override fun draw() {
        glUniformMatrix4fv(
            shader.uniformModelView,
            1,
            false,
            modelMatrix.model,
            0
        )
        drawer.draw()
    }

}