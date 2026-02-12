package good.damn.engine2.opengl.drawmodes

import good.damn.apigl.GLRenderVars
import good.damn.apigl.drawers.GLDrawerLightPass
import good.damn.apigl.enums.GLEnumDrawModeMesh
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.engine2.providers.MGProviderGL

class MGDrawModeTexture(
    lightPassDrawer: GLDrawerLightPass,
    drawModeMesh: GLEnumDrawModeMesh,
    private val lightPassShader: GLShaderLightPass
): MGDrawModeBase(
    lightPassDrawer,
    drawModeMesh
) {

    override fun draw(
        width: Int,
        height: Int
    ) {
        glProvider.apply {
            drawers.drawerFramebuffer.bind()

            geometry.meshSky.draw()
            geometry.drawMeshes()
            geometry.drawMeshesInstanced()
            drawers.drawerFramebuffer.unbind(
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
}