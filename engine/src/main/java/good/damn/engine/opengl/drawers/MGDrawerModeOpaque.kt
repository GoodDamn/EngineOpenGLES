package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.managers.MGIManagerTrigger
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTrigger
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import java.util.concurrent.ConcurrentLinkedQueue

data class MGDrawerModeOpaque(
    var shaderSky: MGShaderSkySphere,
    var shaderOpaque: MGShaderDefault,
    var shaderOpaqueInstanced: MGShaderOpaque,
    var shaderTrigger: MGShaderSingleMode,
    var sky: MGDrawerMeshTexture,
    var camera: MGCamera,
    var directionalLight: MGDrawerLightDirectional,
    var meshes: ConcurrentLinkedQueue<MGDrawerMeshTexture>,
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
        sky.draw()



        shaderOpaque.use()
        camera.draw(
            shaderOpaque
        )
        camera.drawPosition(
            shaderOpaque
        )
        directionalLight.draw(
            shaderOpaque.lightDirectional
        )
        meshes.forEach {
            it.draw()
        }
        lights.draw(
            shaderOpaque.lightPoints
        )




        shaderOpaqueInstanced.use()
        camera.draw(
            shaderOpaqueInstanced
        )
        camera.drawPosition(
            shaderOpaqueInstanced
        )
        directionalLight.draw(
            shaderOpaqueInstanced.lightDirectional
        )

        meshesInstanced.forEach {
            it.draw()
        }

        lights.draw(
            shaderOpaqueInstanced.lightPoints
        )



        if (!canDrawTriggers) {
            return
        }

        shaderTrigger.use()
        camera.draw(
            shaderTrigger
        )
        managersTrigger.forEach {
            it.draw()
        }
    }

}