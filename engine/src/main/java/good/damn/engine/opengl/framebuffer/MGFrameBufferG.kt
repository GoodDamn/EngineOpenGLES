package good.damn.engine.opengl.framebuffer

import android.opengl.GLES30
import android.util.Log
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureActive
import good.damn.engine.opengl.textures.MGTextureAttachment

class MGFrameBufferG(
    private val framebuffer: MGFramebuffer
) {

    companion object {
        private const val TAG = "MGFrameBufferG"
    }

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
        framebuffer.generate()
        framebuffer.bind()

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

        if (!framebuffer.isComplete()) {
            Log.d(TAG, "generate: frame buffer error")
        }

        framebuffer.unbind()
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
        framebuffer.attachColorTexture(
            this
        )
    }

}