package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderMaterial
: MGIUniform {

    var uniformSpecular = 0
        private set

    var uniformShininess = 0
        private set

    var uniformIntensityLight = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformSpecular = glGetUniformLocation(
            program,
            "specularIntensity"
        )

        uniformShininess = glGetUniformLocation(
            program,
            "shine"
        )

        uniformIntensityLight = glGetUniformLocation(
            program,
            "light.intensity"
        )
    }
}