package good.damn.engine2.opengl.drawmodes

import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumDrawMode
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.textures.GLTexture
import good.damn.apigl.textures.GLTextureAttachment
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
                providerModel.drawers.drawerQuad
            ),
            providerModel.shaders.lightPasses[
                GLEnumDrawMode.OPAQUE.ordinal
            ].shader
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentColorSpec,
            GLEnumDrawMode.DIFFUSE,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentDepth,
            GLEnumDrawMode.DEPTH,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentNormal,
            GLEnumDrawMode.NORMAL,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentColorSpec,
            GLEnumDrawMode.DIFFUSE,
            providerModel,
            GLEnumDrawModeMesh.LINES
        )
    )

    val switcherDrawMode = MGRunglCycleDrawerModes(
        mDrawModes
    )

    private inline fun createTextureDrawMode(
        texture: GLTextureAttachment,
        drawMode: GLEnumDrawMode,
        providerModel: MGMProviderGL,
        drawModeMesh: GLEnumDrawModeMesh
    ) = MGDrawModeTexture(
        GLDrawerLightPass(
            arrayOf(
                texture.texture
            ),
            providerModel.drawers.drawerQuad
        ),
        drawModeMesh,
        providerModel.shaders.lightPasses[
            drawMode.ordinal
        ].shader
    )

}