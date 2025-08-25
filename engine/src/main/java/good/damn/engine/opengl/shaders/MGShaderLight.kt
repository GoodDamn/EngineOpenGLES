package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderLight
: MGIUniform {
    var uniformColor = 0
        private set

    var uniformAmbient = 0
        private set

    var uniformPosition = 0
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

        uniformPosition = glGetUniformLocation(
            program,
            "light.position"
        )

        uniformIntensity = glGetUniformLocation(
            program,
            "light.intensity"
        )
    }
}