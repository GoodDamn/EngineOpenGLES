package good.damn.engine.opengl.drawers.light

import android.opengl.GLES30
import good.damn.engine.sdk.SDVector3
import good.damn.engine.opengl.shaders.MGShaderLightPoint

class MGDrawerLightPoint {

    var isActive = 1
    var constant = 1.0f
    var linear = 0.014f
    var quad = 0.0007f
    var radius = 600f
    var alpha = 1.0f
    val position = SDVector3(0f)
    val color = SDVector3(0f)

    fun draw(
        shader: MGShaderLightPoint
    ) {
        GLES30.glUniform1i(
            shader.uniformActive,
            isActive
        )

        GLES30.glUniform1f(
            shader.uniformConstant,
            constant
        )

        GLES30.glUniform1f(
            shader.uniformLinear,
            linear
        )

        GLES30.glUniform1f(
            shader.uniformQuad,
            quad
        )

        GLES30.glUniform3f(
            shader.uniformPosition,
            position.x,
            position.y,
            position.z,
        )

        GLES30.glUniform4f(
            shader.uniformColor,
            color.x,
            color.y,
            color.z,
            alpha
        )

        GLES30.glUniform1f(
            shader.uniformRadius,
            radius
        )
    }
}