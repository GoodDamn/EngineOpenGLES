package good.damn.apigl.shaders

import android.opengl.GLES30

class GLShaderTexture(
    private val name: String
): GLIShaderTexture {

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