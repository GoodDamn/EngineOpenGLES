package good.damn.engine2.opengl

import good.damn.apigl.drawers.GLDrawerFramebufferG
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.drawers.GLIDrawer
import good.damn.apigl.shaders.lightpass.GLShaderLightPass

class MGDrawModeTexture(
    private val lightPassDrawer: GLDrawerLightPass,
    private val lightPassShader: GLShaderLightPass,
    private val drawerFramebufferG: GLDrawerFramebufferG,
    private val geometry: MGMGeometry
): GLIDrawer {

    var canDrawSky = true

    override fun draw(
        width: Int,
        height: Int
    ) {
        drawerFramebufferG.bind()

        if (canDrawSky) {
            geometry.meshSky.draw()
        }
        geometry.drawMeshes()
        geometry.drawMeshesInstanced()
        drawerFramebufferG.unbind(
            width, height
        )

        lightPassShader.run {
            use()
            lightPassDrawer.draw(
                textures
            )
        }

    }
}