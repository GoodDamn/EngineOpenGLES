package good.damn.engine.opengl.drawers

import android.opengl.GLES30.*
import androidx.annotation.CallSuper
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderCamera
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