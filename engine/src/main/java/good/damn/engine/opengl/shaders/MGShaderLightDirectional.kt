package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderLightDirectional
: MGIUniform {
    var uniformColor = 0
        private set

    var uniformAmbient = 0
        private set

    var uniformDirection = 0
        private set

    var uniformIntensity = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformColor = glGetUniformLocation(
            program,
            "dirLight.color"
        )

        uniformAmbient = glGetUniformLocation(
            program,
            "dirLight.factorAmbient"
        )

        uniformDirection = glGetUniformLocation(
            program,
            "dirLight.direction"
        )

        uniformIntensity = glGetUniformLocation(
            program,
            "dirLight.intensity"
        )
    }
}