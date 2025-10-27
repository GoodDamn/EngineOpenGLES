package good.damn.engine.opengl.shaders

import android.opengl.GLES30

class MGShaderTexture
: MGIShaderTexture {

    override var uniformTexture = 0
        private set

    override fun setupUniforms(
        program: Int,
        name: String
    ) {
        uniformTexture = GLES30.glGetUniformLocation(
            program,
            name
        )
    }
}