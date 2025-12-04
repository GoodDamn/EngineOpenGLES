package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderMaterial(
    private val nameShine: String,
    val textures: Array<MGShaderTexture>
): MGIUniform {

    companion object {
        @JvmStatic
        fun singleMaterial(
            textures: Array<MGShaderTexture>
        ) = arrayOf(
            MGShaderMaterial(
                "shine",
                textures
            )
        )
    }

    var uniformShininess = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        textures.forEach {
            it.setupUniforms(
                program
            )
        }

        uniformShininess = glGetUniformLocation(
            program,
            nameShine
        )
    }
}