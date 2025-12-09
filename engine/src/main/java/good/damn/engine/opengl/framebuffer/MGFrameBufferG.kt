package good.damn.engine.opengl.framebuffer

import android.opengl.GLES30
import android.util.Log
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive
import good.damn.engine.opengl.textures.MGTextureAttachment

class MGFrameBufferG {

    companion object {
        private const val TAG = "MGFrameBufferG"
    }

    private val mFramebuffer = MGFramebuffer()
    private val mRenderBuffer = MGRenderBuffer()

    private val mTextureAttachmentPosition = MGTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT0,
        MGTexture(
            MGTextureActive(0)
        )
    )


    private val mTextureAttachmentNormal = MGTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT1,
        MGTexture(
            MGTextureActive(1)
        )
    )


    private val mTextureAttachmentColorSpec = MGTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT2,
        MGTexture(
            MGTextureActive(2)
        )
    )

    fun generate(
        width: Int,
        height: Int
    ) {
        mFramebuffer.generate()
        mFramebuffer.bind()

        val config = MGTextureAttachment.MGMConfig(
            GLES30.GL_RGBA16F,
            GLES30.GL_RGBA,
            GLES30.GL_FLOAT
        )

        generateAttachment(
            mTextureAttachmentPosition,
            width, height,
            config
        )

        generateAttachment(
            mTextureAttachmentNormal,
            width, height,
            config
        )

        generateAttachment(
            mTextureAttachmentColorSpec,
            width, height,
            MGTextureAttachment.MGMConfig(
                GLES30.GL_RGBA,
                GLES30.GL_RGBA,
                GLES30.GL_UNSIGNED_BYTE
            )
        )

        val attachments = intArrayOf(
            mTextureAttachmentPosition.attachment,
            mTextureAttachmentNormal.attachment,
            mTextureAttachmentColorSpec.attachment,
        )

        GLES30.glDrawBuffers(
            attachments.size,
            attachments,
            0
        )

        mRenderBuffer.generate()
        mRenderBuffer.bind()
        mRenderBuffer.allocateStorage(
            width, height
        )

        if (!mFramebuffer.isComplete()) {
            Log.d(TAG, "generate: frame buffer error")
        }

        mFramebuffer.unbind()
    }

    private inline fun generateAttachment(
        textureAttachment: MGTextureAttachment,
        width: Int,
        height: Int,
        config: MGTextureAttachment.MGMConfig
    ) = textureAttachment.run {
        texture.generate()
        glTextureSetup(
            width,
            height,
            config
        )

        texture.bind()
        mFramebuffer.attachColorTexture(
            this
        )
    }

}