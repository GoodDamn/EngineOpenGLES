package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLShaderLightPoint
import good.damn.engine.sdk.matrices.SDMatrixTranslate
import good.damn.engine.sdk.models.SDMLightPoint

class GLDrawerLightPoint(
    var modelMatrix: SDMatrixTranslate,
    var light: SDMLightPoint
) {
    var isActive = false

    companion object {
        @JvmStatic
        fun draw(
            shader: GLShaderLightPoint,
            shaderModel: GLIShaderModel,
            drawer: GLDrawerLightPoint
        ) {
            drawer.light.interpolation.apply {
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

            drawer.modelMatrix.apply {
                GLES30.glUniform3f(
                    shader.uniformPosition,
                    x, y, z
                )
            }

            drawer.light.color.apply {
                GLES30.glUniform4f(
                    shader.uniformColor,
                    x, y, z,
                    drawer.light.alpha
                )
            }

            GLDrawerPositionEntity.draw(
                shaderModel,
                drawer.modelMatrix
            )
        }
    }
}