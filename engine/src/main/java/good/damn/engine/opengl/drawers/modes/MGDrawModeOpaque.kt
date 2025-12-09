package good.damn.engine.opengl.drawers.modes

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.framebuffer.MGFramebuffer

data class MGDrawModeOpaque(
    private val informator: MGMInformator,
    private val framebufferG: MGFramebuffer
): MGIDrawer {

    private val mTriggerManagers = arrayOf(
        informator.managerTrigger,
        informator.managerTriggerLight
    )

    override fun draw() {
        val camera = informator.camera
        val drawerLightDirectional = informator.drawerLightDirectional
        val managerLight = informator.managerLight

        // Geometry pass
        framebufferG.bind()
        informator.meshesInstanced.forEach {
            it.key.run {
                use()
                camera.draw(
                    this
                )
                /*camera.drawPosition(
                    this
                )
                drawerLightDirectional.draw(
                    lightDirectional
                )*/

                it.value.forEach {
                    it.draw(
                        materials
                    )
                }

                /*managerLight.draw(
                    lightPoints
                )*/
            }
        }
        framebufferG.unbind()
        // Light (final) pass

        /*val shaderSky = informator.shaders.sky
        val shaderTrigger = informator.shaders.wireframe.single

        shaderSky.use()
        camera.draw(
            shaderSky
        )
        drawerLightDirectional.drawColor(
            shaderSky.uniformColor
        )

        informator.meshSky.drawSingleTexture(
            shaderSky,
            shaderSky
        )

        informator.meshes.forEach {
            it.key.run {
                use()
                camera.draw(
                    this
                )
                camera.drawPosition(
                    this
                )
                drawerLightDirectional.draw(
                    lightDirectional
                )
                it.value.forEach {
                    it.drawNormals(
                        this
                    )
                    it.drawMaterial(
                        material,
                        this
                    )
                }
                managerLight.draw(
                    lightPoints
                )
            }
        }*/

        /*if (!informator.canDrawTriggers) {
            return
        }

        shaderTrigger.use()
        camera.draw(
            shaderTrigger
        )
        mTriggerManagers.forEach {
            it.draw(
                shaderTrigger
            )
        }*/
    }

}