package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderLight
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
            "light.color"
        )

        uniformAmbient = glGetUniformLocation(
            program,
            "light.factorAmbient"
        )

        uniformDirection = glGetUniformLocation(
            program,
            "light.direction"
        )

        uniformIntensity = glGetUniformLocation(
            program,
            "light.intensity"
        )
    }
}