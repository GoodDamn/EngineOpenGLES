package good.damn.engine2.opengl.drawmodes

import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumLightPass
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.textures.GLTextureAttachment
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
                GLEnumLightPass.OPAQUE.ordinal
            ].shader
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentColorSpec,
            GLEnumLightPass.DIFFUSE,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentDepth,
            GLEnumLightPass.DEPTH,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentNormal,
            GLEnumLightPass.NORMAL,
            providerModel,
            GLEnumDrawModeMesh.TRIANGLES
        ),
        createTextureDrawMode(
            framebufferG.textureAttachmentColorSpec,
            GLEnumLightPass.DIFFUSE,
            providerModel,
            GLEnumDrawModeMesh.LINES
        )
    )

    val switcherDrawMode = MGRunglCycleDrawerModes(
        mDrawModes
    )

    private inline fun createTextureDrawMode(
        texture: GLTextureAttachment,
        drawMode: GLEnumLightPass,
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