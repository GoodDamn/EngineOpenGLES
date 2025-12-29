package good.damn.engine.opengl

import good.damn.engine.MGEngine
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.drawers.MGDrawerLightPass
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.modes.MGDrawModeOpaque
import good.damn.engine.opengl.drawers.modes.MGDrawModeTexture
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGShaderLightPass

class MGSwitcherDrawMode(
    private val informator: MGMInformator,
    drawerFramebufferG: MGDrawerFramebufferG
) {
    private val drawerModeOpaque = MGDrawModeOpaque(
        informator,
        MGDrawerLightPass(
            arrayOf(
                informator.framebufferG.textureAttachmentPosition.texture,
                informator.framebufferG.textureAttachmentNormal.texture,
                informator.framebufferG.textureAttachmentColorSpec.texture,
                informator.framebufferG.textureAttachmentMisc.texture,
                informator.framebufferG.textureAttachmentDepth.texture,
            ),
            informator.drawerQuad
        ),
        informator.shaders.lightPasses[0].shader,
        drawerFramebufferG
    )

    /*private val drawerModeWireframe = MGDrawModeSingleShader(
        informator.shaders.wireframe,
        informator
    )*/

    private val drawerModeDiffuse = MGDrawModeTexture(
        informator,
        MGDrawerLightPass(
            arrayOf(
                informator.framebufferG.textureAttachmentColorSpec.texture
            ),
            informator.drawerQuad
        ),
        informator.shaders.lightPasses[1].shader,
        drawerFramebufferG
    )

    private val drawerModeDepth = MGDrawModeTexture(
        informator,
        MGDrawerLightPass(
            arrayOf(
                informator.framebufferG.textureAttachmentDepth.texture
            ),
            informator.drawerQuad
        ),
        informator.shaders.lightPasses[2].shader,
        drawerFramebufferG
    )

    var currentDrawerMode: MGIDrawer = drawerModeOpaque
        private set

    fun switchDrawMode() = when (
        MGEngine.drawMode
    ) {
        MGEnumDrawMode.OPAQUE -> switchDrawMode(
            MGEnumDrawMode.DIFFUSE,
            drawerModeDiffuse
        )



        else -> switchDrawMode(
            MGEnumDrawMode.OPAQUE,
            drawerModeOpaque
        )
    }


    private fun switchDrawMode(
        drawMode: MGEnumDrawMode,
        currentDrawer: MGIDrawer
    ) {
        MGEngine.drawMode = drawMode
        currentDrawerMode = currentDrawer
    }
}