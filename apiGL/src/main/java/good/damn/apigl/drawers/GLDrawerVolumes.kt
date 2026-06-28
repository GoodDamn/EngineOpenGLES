package good.damn.apigl.drawers

import android.opengl.GLES20.GL_CULL_FACE
import android.opengl.GLES20.glDisable
import android.opengl.GLES30
import androidx.core.util.forEach
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.common.volume.COManagerFrustrum

class GLDrawerVolumes(
    private val drawerPrimitive: GLDrawerVertexArray,
    private val managerVolumes: COManagerFrustrum
): GLIDrawerShader<GLIShaderModel> {

    override fun draw(
        shader: GLIShaderModel
    ) {
        glDisable(
            GL_CULL_FACE
        )

        managerVolumes.volumes.forEach {
            GLDrawerPositionEntity.draw(
                shader,
                it.modelMatrix
            )

            drawerPrimitive.draw(
                GLEnumDrawModeMesh.LINES.v
            )
        }
    }

}