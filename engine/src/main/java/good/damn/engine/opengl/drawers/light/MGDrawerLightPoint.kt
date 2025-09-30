package good.damn.engine.opengl.drawers.light

import android.opengl.GLES30
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderLightPoint

class MGDrawerLightPoint(
    var shader: MGShaderLightPoint
): MGIDrawer {

    var isActive = 1
    var constant = 1.0f
    var linear = 0.0014f
    var quad = 0.000007f
    var radius = 600f
    val position = MGVector(0f)
    val color = MGVector(0f)

    override fun draw() {
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

        GLES30.glUniform3f(
            shader.uniformColor,
            color.x,
            color.y,
            color.z,
        )

        GLES30.glUniform1f(
            shader.uniformRadius,
            radius
        )
    }
}