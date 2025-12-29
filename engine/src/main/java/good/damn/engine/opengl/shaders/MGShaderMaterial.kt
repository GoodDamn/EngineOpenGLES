package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderMaterial(
    val textures: Array<MGShaderTexture>
): MGIUniform {

    companion object {
        @JvmStatic
        fun singleMaterial(
            textures: Array<MGShaderTexture>
        ) = arrayOf(
            MGShaderMaterial(
                textures
            )
        )
    }

    override fun setupUniforms(
        program: Int
    ) {
        textures.forEach {
            it.setupUniforms(
                program
            )
        }
    }
}