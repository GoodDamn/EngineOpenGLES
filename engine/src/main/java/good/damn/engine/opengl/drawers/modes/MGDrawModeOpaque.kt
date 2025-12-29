package good.damn.engine.opengl.drawers.modes

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.drawers.MGDrawerLightPass
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderLightPass

class MGDrawModeOpaque(
    private val informator: MGMInformator,
    private val lightPassDrawer: MGDrawerLightPass,
    private val lightPassShader: MGShaderLightPass,
    private val drawerFramebufferG: MGDrawerFramebufferG
): MGIDrawer {

    private val mTriggerManagers = arrayOf(
        informator.managerTrigger,
        informator.managerLightVolumes
    )

    override fun draw(
        width: Int,
        height: Int
    ) {
        val camera = informator.camera
        val drawerLightDirectional = informator.drawerLightDirectional

        // Geometry pass
        drawerFramebufferG.bind()
        informator.meshSky.meshMaterial.run {
            shader.run {
                use()
                drawer.drawMaterials(
                    materials,
                    this
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

        informator.meshesInstanced.forEach {
            it.shader.run {
                use()
                it.drawer.draw(
                    materials
                )
            }
        }

        if (informator.canDrawTriggers) {
            informator.shaders.wireframe.apply {
                use()
                mTriggerManagers.forEach {
                    it.draw(
                        this
                    )
                }
            }
        }

        drawerFramebufferG.unbind(
            width,
            height
        )

        lightPassShader.run {
            use()
            camera.drawPosition(
                this
            )

            drawerLightDirectional.draw(
                lightDirectional
            )

            lightPassDrawer.draw(
                this
            )
            informator.managerLight.draw(
                lightPoints
            )
        }
    }

}