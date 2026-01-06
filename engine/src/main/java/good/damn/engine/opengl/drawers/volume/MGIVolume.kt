package good.damn.engine.opengl.drawers.volume

import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.matrices.MGMatrixModel
import good.damn.engine.opengl.shaders.MGIShaderModel

interface MGIVolume {
    val drawerModel: MGDrawerPositionEntity

    fun isOnFrustrum(
        v: Boolean
    )
}