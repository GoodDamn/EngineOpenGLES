package good.damn.engine.opengl

import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.glDisable
import good.damn.apigl.drawers.GLDrawerVolumes
import good.damn.apigl.drawers.GLIDrawerShader
import good.damn.apigl.shaders.GLIShaderModel

data class MGMVolume(
    private val drawerVolumes: GLDrawerVolumes
): GLIDrawerShader<
    GLIShaderModel
> {
    var canDrawTriggers = false

    override fun draw(
        shader: GLIShaderModel
    ) {
        if (!canDrawTriggers) {
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