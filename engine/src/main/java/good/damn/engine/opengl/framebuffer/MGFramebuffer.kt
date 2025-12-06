package good.damn.engine.opengl.framebuffer

import android.opengl.GLES30.*
import android.util.Log
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureAttachment

class MGFramebuffer {

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


    fun attachColorTexture(
        texture: MGTextureAttachment
    ) {
        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            texture.texture.id,
            0
        )

        if (glCheckFramebufferStatus(
                GL_FRAMEBUFFER
            ) != GL_FRAMEBUFFER_COMPLETE) {
            Log.d(TAG, "generateTextureAttachment: frame buffer error")
        }
    }

}