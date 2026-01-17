package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLShaderLightPoint
import good.damn.common.matrices.COMatrixTranslate
import good.damn.engine.sdk.models.SDMLightPoint

class GLDrawerLightPoint(
    private val modelMatrix: COMatrixTranslate,
    private val light: SDMLightPoint
) {
    var isActive = false

    fun draw(
        shader: GLShaderLightPoint
    ) {
        light.interpolation.apply {
            GLES30.glUniform1f(
                shader.uniformConstant,
                constant
            )

            GLES30.glUniform1f(
                shader.uniformLinear,
                linear
            )

            GLES30.glUniform1f(
                shader.uniformRadius,
                radius
            )
        }

        modelMatrix.apply {
            GLES30.glUniform3f(
                shader.uniformPosition,
                x, y, z
            )
        }

        light.color.apply {
            GLES30.glUniform4f(
                shader.uniformColor,
                x, y, z,
                light.alpha
            )
        }
    }
}