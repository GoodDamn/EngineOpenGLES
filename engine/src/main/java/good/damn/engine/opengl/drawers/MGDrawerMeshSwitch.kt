package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGIShaderNormal

open class MGDrawerMeshSwitch(
    private val drawSwitch: MGDrawerModeSwitch,
    private val drawEntity: MGDrawerPositionEntity,
): MGIDrawer {

    private var mShaderNormals: MGIShaderNormal? = null

    fun switchDrawMode(
        shader: MGIShaderCamera,
        shaderNormals: MGIShaderNormal?,
        drawMode: MGEnumDrawMode
    ) {
        drawEntity.shader = shader
        mShaderNormals = shaderNormals
        drawSwitch.switchDrawMode(
            drawMode
        )
    }

    override fun draw() {
        mShaderNormals?.run {
            GLES30.glUniformMatrix4fv(
                uniformNormalMatrix,
                1,
                false,
                drawEntity.modelMatrix.normalMatrix,
                0
            )
        }
        drawEntity.draw()
    }
}