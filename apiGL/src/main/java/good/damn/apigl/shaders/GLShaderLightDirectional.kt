package good.damn.apigl.shaders

import android.opengl.GLES30.glGetUniformLocation

class GLShaderLightDirectional
: good.damn.apigl.drawers.GLIUniform {
    var uniformColor = 0
        private set

    var uniformColorAmbient = 0
        private set

    var uniformDirection = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformColor = glGetUniformLocation(
            program,
            "dirLight.color"
        )

        uniformDirection = glGetUniformLocation(
            program,
            "dirLight.direction"
        )

        uniformColorAmbient = glGetUniformLocation(
            program,
            "dirLight.ambientColor"
        )
    }
}