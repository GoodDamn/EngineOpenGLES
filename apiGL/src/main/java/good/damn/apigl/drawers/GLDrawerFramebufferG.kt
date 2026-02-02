package good.damn.apigl.drawers

import android.opengl.GLES30
import android.opengl.GLES30.GL_BLEND
import android.opengl.GLES30.GL_COLOR_BUFFER_BIT
import android.opengl.GLES30.GL_CULL_FACE
import android.opengl.GLES30.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES30.GL_DEPTH_TEST
import android.opengl.GLES30.GL_LINEAR
import android.opengl.GLES30.glClear
import android.opengl.GLES30.glDepthMask
import android.opengl.GLES30.glDisable
import android.opengl.GLES30.glEnable
import android.opengl.GLES30.glReadBuffer
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.textures.GLTextureAttachment

class GLDrawerFramebufferG(
    private val framebufferG: GLFrameBufferG
) {

    fun bind() {
        framebufferG.framebuffer.bindWriting()
        glDepthMask(
            true
        )
        glClear(
            GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
        )
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
        glDisable(
            GL_BLEND
        )
    }

    fun unbind(
        width: Int,
        height: Int
    ) {
        glDepthMask(
            false
        )

        glDisable(GL_DEPTH_TEST)
        framebufferG.framebuffer.unbind()
        // Light (final) pass
        glClear(
            GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT
        )
        glDisable(GL_CULL_FACE)

        glEnable(
            GL_BLEND
        )

        GLES30.glBlendEquation(
            GLES30.GL_FUNC_ADD
        )

        GLES30.glBlendFunc(
            GLES30.GL_ONE,
            GLES30.GL_ONE
        )

        /*framebufferG.framebuffer.bindReading()

        val halfWidth = width / 2
        val halfHeight = height / 2

        glReadTextureCopy(
            framebufferG.textureAttachmentPosition,
            0, 0,
            width, height,
            0, 0,
            halfWidth, halfHeight
        )

        glReadTextureCopy(
            framebufferG.textureAttachmentColorSpec,
            0, 0,
            width, height,
            0, halfHeight,
            halfWidth, height
        )

        glReadTextureCopy(
            framebufferG.textureAttachmentDepth,
            0, 0,
            width, height,
            halfWidth, halfHeight,
            width, height
        )*/
    }

    private inline fun glReadTextureCopy(
        attachment: GLTextureAttachment,
        readX: Int, readY: Int,
        readBufferX: Int, readBufferY: Int,
        readDestX: Int, readDestY: Int,
        readBufferDestX: Int, readBufferDestY: Int
    ) {
        glReadBuffer(
            attachment.attachment
        )

        GLES30.glBlitFramebuffer(
            readX, readY,
            readBufferX, readBufferY,
            readDestX, readDestY,
            readBufferDestX, readBufferDestY,
            GL_COLOR_BUFFER_BIT,
            GL_LINEAR
        )
    }

}