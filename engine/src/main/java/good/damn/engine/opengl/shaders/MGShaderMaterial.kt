package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.drawers.MGIUniform

class MGShaderMaterial(
    val textures: Array<MGShaderTexture>
): MGIUniform {

    companion object {
        @JvmField
        val empty = arrayOf(
            MGShaderMaterial(
                arrayOf()
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