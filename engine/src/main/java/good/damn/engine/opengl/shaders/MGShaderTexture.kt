package good.damn.engine.opengl.shaders

import android.opengl.GLES30

class MGShaderTexture(
    private val name: String
): MGIShaderTexture {

    override var uniformTexture = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformTexture = GLES30.glGetUniformLocation(
            program,
            name
        )
    }
}