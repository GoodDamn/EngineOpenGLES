package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderPostProcess

class MGPostProcess {

    fun draw(
        shader: MGShaderPostProcess
    ) {
        glBindFramebuffer(
            GL_FRAMEBUFFER,
            0
        )

        glClearColor(
            1.0f,
            1.0f,
            0.0f,
            1.0f
        )

        glClear(
            GL_COLOR_BUFFER_BIT
        )
        // use post process shader
        shader.use()
        glDisable(
            GL_DEPTH_TEST
        )
    }
}