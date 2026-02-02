package good.damn.apigl.shaders

import android.opengl.GLES30
import good.damn.apigl.shaders.base.GLShaderBase

class GLShaderPostProcess
: GLShaderBase(),
    GLIShaderTextureUniform {

    override var uniformTexture = -1
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformTexture = GLES30.glGetUniformLocation(
            program,
            "targetTexture"
        )
    }
}