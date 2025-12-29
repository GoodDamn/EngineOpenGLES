package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation

class MGShaderProjectionViewModelTexture
: MGShaderProjectionViewTexture(),
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