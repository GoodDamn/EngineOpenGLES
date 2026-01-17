package good.damn.apigl.shaders

import android.opengl.GLES30

open class GLShaderProjectionViewTexture
: GLShaderProjectionView(),
    GLIShaderTextureUniform {

    final override var uniformTexture = -1
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformTexture = GLES30.glGetUniformLocation(
            program,
            "targetTexture"
        )
        super.setupUniforms(
            program
        )
    }
}