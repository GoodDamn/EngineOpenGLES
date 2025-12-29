package good.damn.engine.opengl.runnables

import android.opengl.GLES30
import android.opengl.GLES30.GL_COLOR_ATTACHMENT0
import android.opengl.GLES30.GL_COLOR_BUFFER_BIT
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES30.GL_DEPTH_TEST
import android.opengl.GLES30.glClear
import android.opengl.GLES30.glClearColor
import android.opengl.GLES30.glEnable
import android.opengl.GLES30.glViewport
import android.util.Log
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.MGSwitcherDrawMode
import good.damn.engine.opengl.entities.MGPostProcess
import good.damn.engine.opengl.framebuffer.MGFramebufferScene
import good.damn.engine.opengl.shaders.MGShaderPostProcess
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive
import good.damn.engine.opengl.textures.MGTextureAttachment

class MGRunnableCycleSwitcherDrawMode(
    private val switcherDrawMode: MGSwitcherDrawMode
): MGIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        switcherDrawMode
            .currentDrawerMode
            .draw(
                width, height
            )
    }
}