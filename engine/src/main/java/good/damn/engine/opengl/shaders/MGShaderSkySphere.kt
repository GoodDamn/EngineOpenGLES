package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetAttribLocation
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUseProgram
import android.util.Log

class MGShaderSkySphere
: MGShaderBase(),
MGIShaderCamera,
MGIShaderTexture,
MGIShaderModel {

    override var uniformTexture = 0
        private set

    override var uniformModelView = 0
        private set

    override var uniformCameraProjection = 0
        private set

    override var uniformCameraView = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        // Uniforms
        uniformTexture = glGetUniformLocation(
            program,
            "texture"
        )

        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )

        uniformCameraProjection = glGetUniformLocation(
            program,
            "projection"
        )

        uniformCameraView = glGetUniformLocation(
            program,
            "view"
        )
    }
}