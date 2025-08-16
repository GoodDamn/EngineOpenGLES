package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform
import good.damn.engine.opengl.shaders.MGIShader

open class MGMesh(
    var shader: MGIShader,
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
    }

}