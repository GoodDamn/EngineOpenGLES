package good.damn.engine.opengl.shaders

import android.opengl.GLES30

class MGShaderSingleMap
: MGShaderSingleMode() {

    var uniformTexture = -1
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformTexture = GLES30.glGetUniformLocation(
            program,
            "texture"
        )
        super.setupUniforms(
            program
        )
    }

}