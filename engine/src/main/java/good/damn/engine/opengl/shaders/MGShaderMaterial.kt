package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderMaterial
: MGIUniform {

    var uniformSpecular = 0
        private set

    var uniformShininess = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        uniformSpecular = glGetUniformLocation(
            program,
            "material.specular"
        )

        uniformShininess = glGetUniformLocation(
            program,
            "material.shine"
        )
    }
}