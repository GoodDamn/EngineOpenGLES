package good.damn.engine.opengl.shaders.base

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGShaderProjectionViewTexture

class MGShaderSky
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