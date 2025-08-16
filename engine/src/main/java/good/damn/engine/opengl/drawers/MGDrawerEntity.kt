package good.damn.engine.opengl.drawers

import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUniformMatrix4fv
import good.damn.engine.opengl.camera.MGMMatrix

open class MGDrawerEntity(
    var modelMatrix: MGMMatrix
): MGIDrawer,
MGIUniform {

    private var mUniformModelView = 0

    override fun setupUniforms(
        program: Int
    ) {
        mUniformModelView = glGetUniformLocation(
            program,
            "model"
        )
    }

    override fun draw() {
        glUniformMatrix4fv(
            mUniformModelView,
            1,
            false,
            modelMatrix.model,
            0
        )
    }
}