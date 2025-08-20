package good.damn.engine.opengl.drawers

import android.opengl.GLES30.*
import androidx.annotation.CallSuper
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.shaders.MGIShader

abstract class MGDrawerPositionEntity(
    private var shader: MGIShader,
    var modelMatrix: MGMMatrix
): MGIDrawer {

    fun setDrawerShader(
        shader: MGIShader
    ) {
        this.shader = shader
    }

    @CallSuper
    override fun draw() {
        glUniformMatrix4fv(
            shader.uniformModelView,
            1,
            false,
            modelMatrix.model,
            0
        )
    }

}