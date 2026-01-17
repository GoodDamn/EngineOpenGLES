package good.damn.apigl.framebuffer

import android.opengl.GLES30.*
import android.util.Log
import good.damn.apigl.textures.GLTextureAttachment

class GLFramebuffer {

    companion object {
        private const val TAG = "MGFramebuffer"
    }

    private val mId = intArrayOf(1)

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

    fun bindWriting() {
        glBindFramebuffer(
            GL_DRAW_FRAMEBUFFER,
            mId[0]
        )
    }

    fun bindReading() {
        glBindFramebuffer(
            GL_READ_FRAMEBUFFER,
            mId[0]
        )
    }

    fun unbindWriting() {
        glBindFramebuffer(
            GL_DRAW_FRAMEBUFFER,
            0
        )
    }

    fun unbindReading() {
        glBindFramebuffer(
            GL_READ_FRAMEBUFFER,
            0
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

    fun isComplete() = glCheckFramebufferStatus(
        GL_FRAMEBUFFER
    ) == GL_FRAMEBUFFER_COMPLETE

    fun attachColorTexture(
        texture: GLTextureAttachment
    ) {
        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            texture.attachment,
            GL_TEXTURE_2D,
            texture.texture.id,
            0
        )

        if (!isComplete()) {
            Log.d(TAG, "generateTextureAttachment: frame buffer error")
        }
    }

}