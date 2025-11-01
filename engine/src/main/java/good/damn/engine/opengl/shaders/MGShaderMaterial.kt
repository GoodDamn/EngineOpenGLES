package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderMaterial
: MGIUniform {

    var uniformShininess = 0
        private set

    val textureDiffuse = MGShaderTexture()
    val textureMetallic = MGShaderTexture()
    val textureEmissive = MGShaderTexture()

    override fun setupUniforms(
        program: Int
    ) {
        textureDiffuse.setupUniforms(
            program,
            "material.textDiffuse"
        )

        textureMetallic.setupUniforms(
            program,
            "material.textMetallic"
        )

        textureEmissive.setupUniforms(
            program,
            "material.textEmissive"
        )

        uniformShininess = glGetUniformLocation(
            program,
            "material.shine"
        )
    }
}