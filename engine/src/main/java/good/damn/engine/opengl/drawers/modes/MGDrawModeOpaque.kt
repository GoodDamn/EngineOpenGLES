package good.damn.engine.opengl.drawers.modes

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
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import java.util.concurrent.ConcurrentLinkedQueue

data class MGDrawModeOpaque(
    var shaderSky: MGShaderSkySphere,
    var shaderOpaque: MGMShader<MGShaderDefault, MGShaderOpaque>,
    var shaderTrigger: MGShaderSingleMode,
    var sky: MGDrawerMeshTextureSwitch,
    var camera: MGCamera,
    var directionalLight: MGDrawerLightDirectional,
    var meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>,
    var meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>,
    var managersTrigger: Array<MGIManagerTrigger>,
    var lights: MGManagerLight
): MGIDrawer {

    var canDrawTriggers = true

    override fun draw() {
        shaderSky.use()
        camera.draw(
            shaderSky
        )
        sky.drawSingleTexture(
            shaderSky.texture,
            shaderSky
        )


        shaderOpaque.single.run {
            use()
            camera.draw(
                this
            )
            camera.drawPosition(
                this
            )
            directionalLight.draw(
                lightDirectional
            )
            meshes.forEach {
                it.drawNormals(
                    this
                )
                it.drawMaterial(
                    material,
                    this
                )
            }
            lights.draw(
                lightPoints
            )
        }



        shaderOpaque.instanced.run {
            use()
            camera.draw(
                this
            )
            camera.drawPosition(
                this
            )
            directionalLight.draw(
                lightDirectional
            )

            meshesInstanced.forEach {
                it.draw(
                    material
                )
            }

            lights.draw(
                lightPoints
            )
        }




        if (!canDrawTriggers) {
            return
        }

        shaderTrigger.use()
        camera.draw(
            shaderTrigger
        )
        managersTrigger.forEach {
            it.draw(
                shaderTrigger
            )
        }
    }

}