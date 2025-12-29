package good.damn.engine.opengl.drawers.modes

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.drawers.MGDrawerLightPass
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderLightPass
import good.damn.engine.opengl.shaders.MGShaderProjectionViewModelTexture
import good.damn.engine.opengl.shaders.MGShaderProjectionViewTexture

class MGDrawModeTexture(
    private val informator: MGMInformator,
    private val lightPassDrawer: MGDrawerLightPass,
    private val lightPassShader: MGShaderLightPass,
    private val drawerFramebufferG: MGDrawerFramebufferG
): MGIDrawer {

    var canDrawSky = true

    override fun draw(
        width: Int,
        height: Int
    ) {
        drawerFramebufferG.bind()

        if (canDrawSky) {
            informator.meshSky.meshMaterial.run {
                shader.run {
                    use()
                    drawer.drawMaterials(
                        materials,
                        this
                    )
                }
            }
        }

        informator.meshesInstanced.forEach {
            it.shader.run {
                use()
                it.drawer.draw(
                    materials
                )
            }
        }

        informator.meshes.forEach {
            it.shader.run {
                use()
                it.drawer.drawNormals(
                    this
                )
                it.drawer.drawMaterials(
                    materials,
                    this
                )
            }
        }

        drawerFramebufferG.unbind(
            width, height
        )

        lightPassShader.run {
            use()
            lightPassDrawer.draw(
                this
            )
        }

    }
}