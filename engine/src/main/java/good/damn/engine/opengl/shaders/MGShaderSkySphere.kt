package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetAttribLocation
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUseProgram
import android.util.Log

class MGShaderSkySphere
: MGShaderBase(),
MGIShaderCamera,
MGIShaderModel {

    override var uniformModelView = 0
        private set

    override var uniformCameraProjection = 0
        private set

    override var uniformCameraView = 0
        private set

    val texture = MGShaderTexture()

    override fun setupUniforms(
        program: Int
    ) {
        // Uniforms
        texture.setupUniforms(
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