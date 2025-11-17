package good.damn.engine.opengl.shaders

import android.opengl.GLES30
import android.opengl.GLES30.glGetUniformLocation

class MGShaderSingleMap
: MGShaderSingleMapInstanced(),
MGIShaderModel {

    final override var uniformModelView = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )
        super.setupUniforms(
            program
        )
    }

}