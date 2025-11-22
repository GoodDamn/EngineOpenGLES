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
    val textureOpacity = MGShaderTexture()
    val textureNormal = MGShaderTexture()

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

        textureOpacity.setupUniforms(
            program,
            "material.textOpacity"
        )

        textureNormal.setupUniforms(
            program,
            "material.textNormal"
        )

        uniformShininess = glGetUniformLocation(
            program,
            "material.shine"
        )
    }
}