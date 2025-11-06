package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.shaders.MGIShaderNormal

class MGDrawerMeshSwitchNormals(
    drawSwitch: MGDrawerModeSwitch,
    drawEntity: MGDrawerPositionEntity,
    private val matrixNormal: MGMatrixNormal
): MGDrawerMeshSwitch(
    drawSwitch,
    drawEntity
) {
    fun switchDrawMode(
        shader: MGIShaderNormal?
    ) {
        matrixNormal.shader = shader
    }

    override fun draw() {
        matrixNormal.draw()
        super.draw()
    }
}