package good.damn.engine.opengl.framebuffer

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

    fun setupAttachments(
        width: Int,
        height: Int
    ) {
        mFramebuffer.bind()
        mFramebuffer.generateTextureAttachment(
            width,
            height
        )
        mRenderbuffer.bind()
        mRenderbuffer.allocateStorage(
            width, height
        )
        mRenderbuffer.unbind()
        mFramebuffer.unbind()
    }
}