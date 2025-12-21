package good.damn.engine.opengl.drawers.modes

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.shaders.MGShaderProjectionViewModelTexture
import good.damn.engine.opengl.shaders.MGShaderProjectionViewTexture

class MGDrawModeTexture(
    private val informator: MGMInformator,
    private val drawerFramebufferG: MGDrawerFramebufferG
): MGIDrawer {

    override fun draw(
        width: Int,
        height: Int
    ) {
        drawerFramebufferG.bind()

        informator.shaders.sky.run {
            use()
            informator.meshSky.drawSingleTexture(
                this,
                this
            )
        }

        informator.meshesInstanced.forEach {
            it.key.run {
                use()
                it.value.forEach {
                    it.draw(materials)
                }
            }
        }
        drawerFramebufferG.unbind(
            width, height
        )

        informator.shaders.lightPassDiffuse.run {
            use()
            informator.drawerLightPassDiffuse.draw(
                this
            )
        }

    }
}