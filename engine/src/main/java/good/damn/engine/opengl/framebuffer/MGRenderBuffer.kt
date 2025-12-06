package good.damn.engine.opengl.framebuffer

import android.opengl.GLES30.*

class MGRenderBuffer {

    val id: Int
        get() = mId[0]

    private val mId = intArrayOf(1)

    fun generate() {
        glGenRenderbuffers(
            1,
            mId,
            0
        )
    }

    fun delete() {
        glDeleteRenderbuffers(
            1,
            mId,
            0
        )
    }

    fun bind() {
        glBindRenderbuffer(
            GL_RENDERBUFFER,
            mId[0]
        )
    }

    fun unbind() {
        glBindRenderbuffer(
            GL_RENDERBUFFER,
            0
        )
    }

    fun allocateStorage(
        width: Int,
        height: Int
    ) {
        glRenderbufferStorage(
            GL_RENDERBUFFER,
            GL_DEPTH24_STENCIL8,
            width,
            height
        )

        unbind()

        glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            GL_DEPTH_STENCIL_ATTACHMENT,
            GL_RENDERBUFFER,
            mId[0]
        )
    }
}