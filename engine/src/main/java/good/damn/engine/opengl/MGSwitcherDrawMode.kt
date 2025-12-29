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
        informator.shaders.lightPasses[
            MGEnumDrawMode.OPAQUE.ordinal
        ].shader,
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
        informator.shaders.lightPasses[
            MGEnumDrawMode.DIFFUSE.ordinal
        ].shader,
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
        informator.shaders.lightPasses[
            MGEnumDrawMode.DEPTH.ordinal
        ].shader,
        drawerFramebufferG
    )

    private val drawerModeNormals = MGDrawModeTexture(
        informator,
        MGDrawerLightPass(
            arrayOf(
                informator.framebufferG.textureAttachmentNormal.texture
            ),
            informator.drawerQuad
        ),
        informator.shaders.lightPasses[
            MGEnumDrawMode.NORMAL.ordinal
        ].shader,
        drawerFramebufferG
    )

    var currentDrawerMode: MGIDrawer = drawerModeOpaque
        private set

    fun switchDrawMode() = when (
        MGEngine.drawMode
    ) {
        MGEnumDrawMode.OPAQUE -> switchDrawMode(
            MGEnumDrawMode.DIFFUSE,
            drawerModeDiffuse.apply {
                canDrawSky = true
            }
        )

        MGEnumDrawMode.DIFFUSE -> switchDrawMode(
            MGEnumDrawMode.DEPTH,
            drawerModeDepth
        )

        MGEnumDrawMode.DEPTH -> switchDrawMode(
            MGEnumDrawMode.NORMAL,
            drawerModeNormals.apply {
                canDrawSky = false
            }
        )

        MGEnumDrawMode.NORMAL -> switchDrawMode(
            MGEnumDrawMode.WIREFRAME,
            drawerModeDiffuse.apply {
                canDrawSky = false
            }
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
        val enableWireframe = drawMode == MGEnumDrawMode.WIREFRAME

        if (drawMode == MGEnumDrawMode.OPAQUE
            || drawMode == MGEnumDrawMode.WIREFRAME
        ) {
            informator.meshes.forEach {
                it.drawer.drawerMesh.setIsWireframe(
                    enableWireframe
                )
            }

            informator.meshesInstanced.forEach {
                it.drawer.setIsWireframe(
                    enableWireframe
                )
            }
        }
    }
}