package good.damn.apigl.shaders

import android.opengl.GLES30.glGetUniformLocation

class GLShaderProjectionViewModelTexture
: GLShaderProjectionViewTexture(),
    GLIShaderModel {

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