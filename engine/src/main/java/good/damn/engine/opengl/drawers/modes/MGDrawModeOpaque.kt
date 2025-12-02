package good.damn.engine.opengl.drawers.modes

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.managers.MGIManagerTrigger
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import java.util.concurrent.ConcurrentLinkedQueue

data class MGDrawModeOpaque(
    private val informator: MGMInformator
): MGIDrawer {

    private val mTriggerManagers = arrayOf(
        informator.managerTrigger,
        informator.managerTriggerLight
    )

    override fun draw() {

        val shaderSky = informator.shaders.map.single
        val shaderTrigger = informator.shaders.wireframe.single
        val camera = informator.camera
        val drawerLightDirectional = informator.drawerLightDirectional
        val managerLight = informator.managerLight

        shaderSky.use()
        camera.draw(
            shaderSky
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
        }


        informator.meshesInstanced.forEach {
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
                    it.draw(
                        material
                    )
                }

                managerLight.draw(
                    lightPoints
                )
            }
        }



        if (!informator.canDrawTriggers) {
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
        }
    }

}