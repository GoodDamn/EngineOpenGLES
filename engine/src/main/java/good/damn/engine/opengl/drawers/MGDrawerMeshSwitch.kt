package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShader

data class MGDrawerMeshSwitch(
    private val drawSwitch: MGDrawerModeSwitch,
    private val drawEntity: MGDrawerPositionEntity
): MGIDrawer {

    fun switchDrawMode(
        shader: MGIShader,
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