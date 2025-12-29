package good.damn.engine.opengl.shaders

import android.opengl.GLES30
import good.damn.engine.opengl.shaders.base.MGShaderBase

class MGShaderPostProcess
: MGShaderBase(),
MGIShaderTextureUniform {

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