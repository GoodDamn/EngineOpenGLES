package good.damn.engine2.opengl

import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.glDisable
import good.damn.apigl.drawers.GLDrawerVolumes
import good.damn.apigl.drawers.GLIDrawerShader
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.engine2.models.MGMParameters

data class MGMVolume(
    private val drawerVolumes: GLDrawerVolumes,
    private val parameters: MGMParameters
): GLIDrawerShader<
    GLIShaderModel
> {

    override fun draw(
        shader: GLIShaderModel
    ) {
        if (!parameters.canDrawTriggers) {
            return
        }

        glDisable(
            GL_CULL_FACE
        )

        drawerVolumes.draw(
            shader
        )
    }
}