package good.damn.engine.opengl.runnables

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
    informator: MGMInformator,
    private val switcherDrawMode: MGSwitcherDrawMode
): MGIRunnableBounds {

    private val mShaderPostProcess = MGShaderPostProcess()

    private val mTextureAttachmentPost = MGTextureAttachment(
        GL_COLOR_ATTACHMENT0,
        MGTexture(
            MGTextureActive(0)
        )
    )
    private val mFramebufferScene = MGFramebufferScene()
    private val mPostProcess = MGPostProcess()

    init {
        informator.glHandler.post(
            object: MGIRunnableBounds {
                override fun run(
                    width: Int,
                    height: Int
                ) {
                    mShaderPostProcess.setup(
                        "shaders/post/vert.glsl",
                        "shaders/post/frag.glsl",
                        MGBinderAttribute.Builder()
                            .bindPosition()
                            .bindTextureCoordinates()
                            .build()
                    )

                    mTextureAttachmentPost.texture.generate()
                    mTextureAttachmentPost.glTextureSetup(
                        width, height
                    )

                    mPostProcess.configure()
                    mFramebufferScene.generate()
                    mFramebufferScene.setupAttachment(
                        mTextureAttachmentPost,
                        width,
                        height
                    )
                }

            }
        )
    }


    override fun run(
        width: Int,
        height: Int
    ) {
        mFramebufferScene.bind()
        glViewport(
            0,
            0,
            width,
            height
        )

        glClear(
            GL_COLOR_BUFFER_BIT or
                GL_DEPTH_BUFFER_BIT
        )

        glClearColor(
            0.0f,
            0.0f,
            0.0f,
            1.0f
        )

        glEnable(
            GL_CULL_FACE
        )

        glEnable(
            GL_DEPTH_TEST
        )

        switcherDrawMode
            .currentDrawerMode
            .draw()

        mFramebufferScene.unbind()

        mPostProcess.draw(
            width,
            height,
            mShaderPostProcess,
            mTextureAttachmentPost
        )
    }
}