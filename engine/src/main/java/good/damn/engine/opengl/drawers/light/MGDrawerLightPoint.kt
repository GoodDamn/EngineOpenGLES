package good.damn.engine.opengl.drawers.light

import android.opengl.GLES30
import good.damn.engine.opengl.shaders.MGShaderLightPoint
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import good.damn.engine.sdk.models.SDMLightPointEntity

class MGDrawerLightPoint(
    private val entity: MGDrawerTriggerStateableLight
) {
    companion object {
        @JvmStatic
        fun drawNull(
            shader: MGShaderLightPoint
        ) {
            GLES30.glUniform1i(
                shader.uniformActive,
                0
            )
        }
    }

    var isActive = 1

    fun draw(
        shader: MGShaderLightPoint
    ) {
        GLES30.glUniform1i(
            shader.uniformActive,
            isActive
        )

        val light = entity.light
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
                shader.uniformQuad,
                quad
            )

            GLES30.glUniform1f(
                shader.uniformRadius,
                radius
            )
        }

        entity.modelMatrix.position.run {
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