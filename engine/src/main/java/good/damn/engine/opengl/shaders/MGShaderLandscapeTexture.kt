package good.damn.engine.opengl.shaders

import good.damn.engine.opengl.shaders.base.MGShaderBase

class MGShaderLandscapeTexture
: MGShaderBase() {

    val textureDiffuse = MGShaderTexture(
        "textDiffuse"
    )

    val textureControl = MGShaderTexture(
        "textControl"
    )

    val textureOutput = MGShaderTexture(
        "targetTexture"
    )

    override fun setupUniforms(
        program: Int
    ) {
        textureDiffuse.setupUniforms(
            program
        )

        textureControl.setupUniforms(
            program
        )

        textureOutput.setupUniforms(
            program
        )
    }
}