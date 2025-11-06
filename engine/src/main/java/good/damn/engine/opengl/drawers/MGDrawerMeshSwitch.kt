package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform

open class MGDrawerMeshSwitch(
    private val drawSwitch: MGDrawerModeSwitch,
    private val drawEntity: MGDrawerPositionEntity,
): MGIDrawer {

    fun switchDrawMode(
        shader: MGIShaderModel,
        shaderTexture: MGIShaderTextureUniform?,
        drawMode: MGEnumDrawMode
    ) {
        drawEntity.shader = shader
        drawSwitch.switchDrawMode(
            drawMode,
            shaderTexture
        )
    }

    override fun draw() {
        drawSwitch.draw()
        drawEntity.draw()
    }
}