package good.damn.engine.opengl.framebuffer

import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureAttachment

class MGFramebufferScene {

    private val mFramebuffer = MGFramebuffer()
    private val mRenderbuffer = MGRenderBuffer()

    fun generate() {
        mFramebuffer.generate()
        mRenderbuffer.generate()
    }

    fun delete() {
        mFramebuffer.delete()
        mRenderbuffer.delete()
    }

    fun bind() {
        mFramebuffer.bind()
    }

    fun unbind() {
        mFramebuffer.unbind()
    }

    fun setupAttachment(
        texture: MGTextureAttachment,
        width: Int,
        height: Int
    ) {
        mFramebuffer.bind()
        mFramebuffer.attachColorTexture(
            texture
        )
        mRenderbuffer.bind()
        mRenderbuffer.allocateStorage(
            width, height
        )
        mRenderbuffer.unbind()
        mFramebuffer.unbind()
    }
}