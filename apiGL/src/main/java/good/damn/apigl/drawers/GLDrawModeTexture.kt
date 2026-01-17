package good.damn.apigl.drawers

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.shaders.lightpass.MGShaderLightPass

class GLDrawModeTexture(
    private val informator: MGMInformator,
    private val lightPassDrawer: good.damn.apigl.drawers.GLDrawerLightPass,
    private val lightPassShader: MGShaderLightPass,
    private val drawerFramebufferG: good.damn.apigl.drawers.GLDrawerFramebufferG
): good.damn.apigl.drawers.GLIDrawer {

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