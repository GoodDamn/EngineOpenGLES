package good.damn.engine2.opengl.drawmodes

import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumDrawMode
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.textures.GLTexture
import good.damn.engine2.providers.MGIProviderGLRegister
import good.damn.engine2.providers.MGMProviderGL

class MGDrawModesDefault(
    framebufferG: GLFrameBufferG,
    providerModel: MGMProviderGL
) {

    private val mDrawModes = arrayOf(
        MGDrawModeOpaque(
            GLDrawerLightPass(
                arrayOf(
                    framebufferG.textureAttachmentPosition.texture,
                    framebufferG.textureAttachmentNormal.texture,
                    framebufferG.textureAttachmentColorSpec.texture,
                    framebufferG.textureAttachmentMisc.texture,
                    framebufferG.textureAttachmentDepth.texture
                ),
                providerModel.geometry.drawerQuad
            ),
            providerModel.shaders.lightPasses[
                GLEnumDrawMode.OPAQUE.ordinal
            ].shader
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentColorSpec.texture,
            GLEnumDrawMode.DIFFUSE,
            providerModel
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentDepth.texture,
            GLEnumDrawMode.DEPTH,
            providerModel
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentNormal.texture,
            GLEnumDrawMode.NORMAL,
            providerModel
        )
    )

    val switcherDrawMode = MGRunglCycleDrawerModes(
        mDrawModes
    )

    private inline fun createTextureDrawMode(
        texture: GLTexture,
        drawMode: GLEnumDrawMode,
        providerModel: MGMProviderGL
    ) = MGDrawModeTexture(
        GLDrawerLightPass(
            arrayOf(
                texture
            ),
            providerModel.geometry.drawerQuad
        ),
        providerModel.shaders.lightPasses[
            drawMode.ordinal
        ].shader
    )

}