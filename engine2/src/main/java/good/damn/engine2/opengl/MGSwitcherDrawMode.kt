package good.damn.engine2.opengl

import good.damn.apigl.GLRenderVars
import good.damn.apigl.drawers.GLDrawerFramebufferG
import good.damn.apigl.drawers.GLDrawerLightDirectional
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.drawers.GLDrawerLights
import good.damn.apigl.drawers.GLIDrawer
import good.damn.apigl.enums.GLEnumDrawMode
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.textures.GLTexture
import good.damn.engine2.models.MGMInformatorShader

class MGSwitcherDrawMode(
    framebufferG: GLFrameBufferG,
    private val shaders: MGMInformatorShader,
    private val geometry: MGMGeometry,
    private val volume: MGMVolume,
    private val drawerLightDirect: GLDrawerLightDirectional,
    drawerFramebufferG: GLDrawerFramebufferG
) {
    private val drawerModeOpaque = arrayOf(
        framebufferG.textureAttachmentPosition.texture,
        framebufferG.textureAttachmentNormal.texture,
        framebufferG.textureAttachmentColorSpec.texture,
        framebufferG.textureAttachmentMisc.texture,
        framebufferG.textureAttachmentDepth.texture,
    ).run {
        MGDrawModeOpaque(
            shaders,
            GLDrawerLightPass(
                this,
                geometry.drawerQuad
            ),
            GLDrawerLights(
                GLDrawerLightPass(
                    this,
                    geometry.drawerSphere
                )
            ),
            shaders.lightPasses[
                GLEnumDrawMode.OPAQUE.ordinal
            ].shader,
            geometry,
            drawerFramebufferG,
            drawerLightDirect,
            volume
        )
    }

    private val drawerModeDiffuse = createTextureDrawMode(
        framebufferG.textureAttachmentColorSpec.texture,
        GLEnumDrawMode.DIFFUSE,
        drawerFramebufferG
    )

    private val drawerModeDepth = createTextureDrawMode(
        framebufferG.textureAttachmentDepth.texture,
        GLEnumDrawMode.DEPTH,
        drawerFramebufferG
    )

    private val drawerModeNormals = createTextureDrawMode(
        framebufferG.textureAttachmentNormal.texture,
        GLEnumDrawMode.NORMAL,
        drawerFramebufferG
    )

    private var mCurrentDrawMode = GLEnumDrawMode.OPAQUE

    var currentDrawerMode: GLIDrawer = drawerModeOpaque
        private set

    fun switchDrawMode() = when (
        mCurrentDrawMode
    ) {
        GLEnumDrawMode.OPAQUE -> switchDrawMode(
            GLEnumDrawMode.DIFFUSE,
            drawerModeDiffuse.apply {
                canDrawSky = true
            }
        )

        GLEnumDrawMode.DIFFUSE -> switchDrawMode(
            GLEnumDrawMode.DEPTH,
            drawerModeDepth
        )

        GLEnumDrawMode.DEPTH -> switchDrawMode(
            GLEnumDrawMode.NORMAL,
            drawerModeNormals.apply {
                canDrawSky = false
            }
        )

        GLEnumDrawMode.NORMAL -> switchDrawMode(
            GLEnumDrawMode.WIREFRAME,
            drawerModeDiffuse.apply {
                canDrawSky = false
            }
        )

        else -> switchDrawMode(
            GLEnumDrawMode.OPAQUE,
            drawerModeOpaque
        )
    }


    private fun switchDrawMode(
        drawMode: GLEnumDrawMode,
        currentDrawer: GLIDrawer
    ) {
        mCurrentDrawMode = drawMode
        currentDrawerMode = currentDrawer
        val enableWireframe = drawMode == GLEnumDrawMode.WIREFRAME
        if (drawMode == GLEnumDrawMode.OPAQUE
            || drawMode == GLEnumDrawMode.WIREFRAME
        ) {
            GLRenderVars.drawModeMesh = if (
                enableWireframe
            ) GLEnumDrawModeMesh.LINES
            else GLEnumDrawModeMesh.TRIANGLES
        }
    }

    private inline fun createTextureDrawMode(
        texture: GLTexture,
        drawMode: GLEnumDrawMode,
        drawerFramebufferG: GLDrawerFramebufferG
    ) = MGDrawModeTexture(
        GLDrawerLightPass(
            arrayOf(
                texture
            ),
            geometry.drawerQuad
        ),
        shaders.lightPasses[
            drawMode.ordinal
        ].shader,
        drawerFramebufferG,
        geometry
    )
}