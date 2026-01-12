package good.damn.engine.opengl.drawers.volume

import android.opengl.GLES30
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.MGIDrawerShader
import good.damn.engine.opengl.shaders.MGIShaderModel

class MGDrawerVolumes(
    private val drawerPrimitive: MGDrawerVertexArray,
    private val managerVolumes: COManagerFrustrum
): MGIDrawerShader<MGIShaderModel> {

    override fun draw(
        shader: MGIShaderModel
    ) {
        managerVolumes.volumes.forEach {
            MGDrawerPositionEntity.draw(
                shader,
                it.modelMatrix
            )

            drawerPrimitive.draw(
                GLES30.GL_LINES
            )
        }
    }

}