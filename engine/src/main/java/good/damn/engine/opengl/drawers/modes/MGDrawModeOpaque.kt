package good.damn.engine.opengl.drawers.modes

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerFramebufferG
import good.damn.engine.opengl.drawers.MGIDrawer

data class MGDrawModeOpaque(
    private val informator: MGMInformator,
    private val drawerFramebufferG: MGDrawerFramebufferG
): MGIDrawer {

    private val mTriggerManagers = arrayOf(
        informator.managerTrigger,
        informator.managerTriggerLight
    )

    override fun draw(
        width: Int,
        height: Int
    ) {
        val camera = informator.camera
        val drawerLightDirectional = informator.drawerLightDirectional

        // Geometry pass
        drawerFramebufferG.bind()
        informator.shaders.sky.run {
            use()
            informator.meshSky.drawMaterials(
                materials,
                this
            )
        }

        informator.meshes.forEach {
            it.shader.run {
                use()
                it.drawNormals(
                    this
                )
                it.drawMaterials(
                    materials,
                    this
                )
            }
        }

        informator.meshesInstanced.forEach {
            it.key.run {
                use()
                it.value.forEach {
                    it.draw(
                        materials
                    )
                }
            }
        }

        /*if (informator.canDrawTriggers) {
            informator.shaders.wireframe.single.run {
                use()
                camera.draw(
                    this
                )
                mTriggerManagers.forEach {
                    it.draw(
                        this
                    )
                }
            }
        }*/

        drawerFramebufferG.unbind(
            width,
            height
        )

        informator.shaders.lightPassOpaque.run {
            use()
            camera.drawPosition(
                this
            )

            drawerLightDirectional.draw(
                lightDirectional
            )

            informator.drawerLightPass.draw(
                this
            )
            informator.managerLight.draw(
                lightPoints
            )
        }
    }

}