package good.damn.engine.opengl.drawers

import android.opengl.GLES30.*
import good.damn.engine.opengl.matrices.MGMMatrix
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGDrawerPositionEntity(
    private val drawer: MGIDrawer,
    var shader: MGIShaderModel,
    var modelMatrix: MGMMatrix
): MGIDrawer {

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