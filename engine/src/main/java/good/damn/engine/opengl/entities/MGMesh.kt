package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform

open class MGMesh(
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