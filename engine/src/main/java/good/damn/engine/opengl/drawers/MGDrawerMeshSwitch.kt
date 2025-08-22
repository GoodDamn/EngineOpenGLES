package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderCamera

open class MGDrawerMeshSwitch(
    private val drawSwitch: MGDrawerModeSwitch,
    private val drawEntity: MGDrawerPositionEntity
): MGIDrawer {

    fun switchDrawMode(
        shader: MGIShaderCamera,
        drawMode: MGEnumDrawMode
    ) {
        drawEntity.shader = shader
        drawSwitch.switchDrawMode(
            drawMode
        )
    }

    override fun draw() {
        drawEntity.draw()
    }
}