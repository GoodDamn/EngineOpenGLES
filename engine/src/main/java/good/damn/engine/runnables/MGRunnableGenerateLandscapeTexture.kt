package good.damn.engine.runnables

import android.opengl.GLES30
import android.util.Size
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMLandscapeTexture
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.framebuffer.MGFramebuffer
import good.damn.engine.opengl.shaders.MGShaderLandscapeTexture
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.opengl.textures.MGTextureAttachment
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsVertIndices

class MGRunnableGenerateLandscapeTexture(
    private val landscapeBounds: Size,
    private val textures: Array<MGMLandscapeTexture>,
    private val shader: MGShaderLandscapeTexture
): Runnable {

    private val mFramebuffer = MGFramebuffer()
    private val mTextureResult = MGTextureAttachment(
        MGTexture(0)
    )

    private val mArrayVertexQuad = MGArrayVertexConfigurator(
        MGEnumArrayVertexConfiguration.BYTE
    )

    private val mDrawerQuad = MGDrawerVertexArray(
        mArrayVertexQuad
    )

    override fun run() {
        mArrayVertexQuad.configure(
            MGUtilsBuffer.createFloat(
                MGUtilsVertIndices.createQuadVertices()
            ),
            MGUtilsBuffer.createByte(
                MGUtilsVertIndices.createQuadIndices()
            ),
            MGPointerAttribute.Builder()
                .pointPosition2()
                .pointTextureCoordinates()
                .build()
        )

        mTextureResult.texture.generate()
        mTextureResult.glTextureSetup(
            landscapeBounds.width,
            landscapeBounds.height
        )

        mFramebuffer.generate()
        mFramebuffer.bind()
        mFramebuffer.attachColorTexture(
            mTextureResult
        )
        mFramebuffer.unbind()

        textures.forEach {
            mFramebuffer.bind()
            GLES30.glClear(
                GLES30.GL_COLOR_BUFFER_BIT
            )

            GLES30.glClearColor(
                1.0f,
                0.0f,
                1.0f,
                1.0f
            )

            GLES30.glViewport(
                0, 0,
                landscapeBounds.width,
                landscapeBounds.height
            )

            shader.use()
            drawTextures(it)
            mDrawerQuad.draw(
                GLES30.GL_TRIANGLES
            )
            unbindTextures(it)

            mFramebuffer.unbind()
        }

    }

    private inline fun drawTextures(
        it: MGMLandscapeTexture
    ) {
        it.diffuse.texture.draw(
            shader.textureDiffuse
        )

        it.control.texture.draw(
            shader.textureControl
        )

        mTextureResult.texture.draw(
            shader.textureOutput
        )
    }

    private inline fun unbindTextures(
        it: MGMLandscapeTexture
    ) {
        it.diffuse.texture.unbind(
            shader.textureDiffuse
        )

        it.control.texture.unbind(
            shader.textureControl
        )

        mTextureResult.texture.unbind(
            shader.textureOutput
        )
    }
}