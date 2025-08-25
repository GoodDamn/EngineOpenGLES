package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation

class MGShaderSingleModeNormals
: MGShaderSingleMode(),
MGIShaderNormal {

    override var uniformNormalMatrix = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformNormalMatrix = glGetUniformLocation(
            program,
            "normalMatrix"
        )
        super.setupUniforms(
            program
        )
    }
}