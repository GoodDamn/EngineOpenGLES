package good.damn.apigl.framebuffer

import android.opengl.GLES30
import android.opengl.GLES30.GL_UNSIGNED_BYTE
import android.util.Log
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureActive
import good.damn.apigl.textures.GLTextureAttachment

class GLFrameBufferG(
    val framebuffer: GLFramebuffer
) {

    companion object {
        private const val TAG = "MGFrameBufferG"
    }

    private val mRenderBuffer = GLRenderBuffer()

    val textureAttachmentPosition = GLTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT0,
        GLTexture(
            GLTextureActive(0)
        )
    )


    val textureAttachmentNormal = GLTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT1,
        GLTexture(
            GLTextureActive(1)
        )
    )


    val textureAttachmentColorSpec = GLTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT2,
        GLTexture(
            GLTextureActive(2)
        )
    )

    val textureAttachmentMisc = GLTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT3,
        GLTexture(
            GLTextureActive(3)
        )
    )

    val textureAttachmentDepth = GLTextureAttachment(
        GLES30.GL_COLOR_ATTACHMENT4,
        GLTexture(
            GLTextureActive(4)
        )
    )

    fun generate(
        width: Int,
        height: Int
    ) {
        framebuffer.generate()
        framebuffer.bindWriting()

        val config = GLTextureAttachment.GLMConfig(
            GLES30.GL_RGBA16F,
            GLES30.GL_RGBA,
            GLES30.GL_FLOAT
        )

        val attachments = intArrayOf(
            textureAttachmentPosition.attachment,
            textureAttachmentNormal.attachment,
            textureAttachmentColorSpec.attachment,
            textureAttachmentMisc.attachment,
            textureAttachmentDepth.attachment
        )

        generateAttachment(
            textureAttachmentPosition,
            width, height,
            config
        )

        generateAttachment(
            textureAttachmentNormal,
            width, height,
            config
        )

        generateAttachment(
            textureAttachmentColorSpec,
            width, height,
            GLTextureAttachment.GLMConfig.rgba
        )

        generateAttachment(
            textureAttachmentMisc,
            width, height,
            GLTextureAttachment.GLMConfig(
                GLES30.GL_R8,
                GLES30.GL_RED,
                GL_UNSIGNED_BYTE
            )
        )

        generateAttachment(
            textureAttachmentDepth,
            width, height,
            GLTextureAttachment.GLMConfig(
                GLES30.GL_R16F,
                GLES30.GL_RED,
                GLES30.GL_FLOAT
            )
        )

        GLES30.glDrawBuffers(
            attachments.size,
            attachments,
            0
        )

        framebuffer.bindWriting()
        mRenderBuffer.generate()
        mRenderBuffer.bind()
        mRenderBuffer.allocateStorage(
            width, height
        )

        if (!framebuffer.isComplete()) {
            Log.d(TAG, "generate: frame buffer error")
        }

        framebuffer.unbindWriting()
    }

    private inline fun generateAttachment(
        textureAttachment: GLTextureAttachment,
        width: Int,
        height: Int,
        config: GLTextureAttachment.GLMConfig
    ) = textureAttachment.run {
        texture.generate()
        texture.bind()
        glTextureSetup(
            width,
            height,
            config
        )

        texture.bind()
        framebuffer.attachColorTexture(
            this
        )
        texture.unbind()
    }

}