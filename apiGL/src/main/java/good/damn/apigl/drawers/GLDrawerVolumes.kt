package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.common.volume.COManagerFrustrum

class GLDrawerVolumes(
    private val drawerPrimitive: GLDrawerVertexArray,
    private val managerVolumes: COManagerFrustrum
): GLIDrawerShader<GLIShaderModel> {

    override fun draw(
        shader: GLIShaderModel
    ) {
        managerVolumes.volumes.forEach {
            GLDrawerPositionEntity.draw(
                shader,
                it.modelMatrix
            )

            drawerPrimitive.draw(
                GLES30.GL_LINES
            )
        }
    }

}