package good.damn.engine.opengl.shaders

import android.opengl.GLES30

open class MGShaderProjectionViewTexture
: MGShaderProjectionView(),
MGIShaderTextureUniform {

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