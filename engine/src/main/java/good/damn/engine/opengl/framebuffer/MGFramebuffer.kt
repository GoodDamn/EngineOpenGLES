package good.damn.engine.opengl.framebuffer

import android.opengl.GLES30.*
import android.util.Log

class MGFramebuffer {

    companion object {
        private const val TAG = "MGFramebuffer"
    }

    val textureId: Int
        get() = mTexture[0]

    private val mId = intArrayOf(1)
    private val mIdRenderBuffer = intArrayOf(1)
    private val mTexture = intArrayOf(1)

    fun generate() {
        if (mId[0] != 1) {
            throw IllegalStateException(
                "Framebuffer is already generated"
            )
        }

        glGenFramebuffers(
            1,
            mId,
            0
        )
    }

    fun bind() {
        glBindFramebuffer(
            GL_FRAMEBUFFER,
            mId[0]
        )
    }

    fun unbind() {
        glBindFramebuffer(
            GL_FRAMEBUFFER,
            0
        )
    }


    fun delete() {
        glDeleteFramebuffers(
            1,
            mId,
            0
        )
        mId[0] = -1
    }


    fun generateTextureAttachment(
        width: Int,
        height: Int
    ) {
        glGenTextures(
            1,
            mTexture,
            0
        )

        glBindTexture(
            GL_TEXTURE_2D,
            mTexture[0]
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MIN_FILTER,
            GL_LINEAR
        )

        glTexParameteri(
            GL_TEXTURE_2D,
            GL_TEXTURE_MAG_FILTER,
            GL_LINEAR
        )

        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGB,
            width,
            height,
            0,
            GL_RGB,
            GL_UNSIGNED_BYTE,
            null
        )

        glBindTexture(
            GL_TEXTURE_2D,
            0
        )

        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            mTexture[0],
            0
        )

        glGenRenderbuffers(
            1,
            mIdRenderBuffer,
            0
        )

        glBindRenderbuffer(
            GL_RENDERBUFFER,
            mIdRenderBuffer[0]
        )

        glRenderbufferStorage(
            GL_RENDERBUFFER,
            GL_DEPTH24_STENCIL8,
            width,
            height
        )

        glBindRenderbuffer(
            GL_RENDERBUFFER,
            0
        )

        glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            GL_DEPTH_STENCIL_ATTACHMENT,
            GL_RENDERBUFFER,
            mIdRenderBuffer[0]
        )

        if (glCheckFramebufferStatus(
                GL_FRAMEBUFFER
            ) != GL_FRAMEBUFFER_COMPLETE) {
            Log.d(TAG, "generateTextureAttachment: frame buffer error")
        }
        unbind()
    }

}