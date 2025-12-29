package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_COLOR_BUFFER_BIT
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES30.GL_DEPTH_TEST
import android.opengl.GLES30.glClear
import android.opengl.GLES30.glDisable
import android.opengl.GLES30.glEnable
import android.opengl.GLES30.glViewport
import good.damn.engine.opengl.framebuffer.MGFramebuffer

class MGDrawerFramebufferG(
    private val framebuffer: MGFramebuffer
) {

    fun bind() {
        framebuffer.bind()
        glClear(
            GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
        )
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
    }

    fun unbind(
        width: Int,
        height: Int
    ) {
        framebuffer.unbind()
        // Light (final) pass
        glClear(
            GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
        )
        glViewport(
            0, 0,
            width, height
        )
        glDisable(GL_CULL_FACE)
        glDisable(GL_DEPTH_TEST)
    }

}