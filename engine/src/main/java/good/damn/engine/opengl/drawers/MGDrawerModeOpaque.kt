package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTrigger
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import java.util.concurrent.ConcurrentLinkedQueue

data class MGDrawerModeOpaque(
    var shaderSky: MGShaderSkySphere,
    var shaderOpaque: MGShaderDefault,
    var shaderTrigger: MGShaderSingleMode,
    var sky: MGMesh,
    var camera: MGCamera,
    var directionalLight: MGDrawerLightDirectional,
    var meshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>,
    var triggers: MGManagerTrigger,
    var triggersLight: ConcurrentLinkedQueue<MGDrawerTriggerStateableLight>,
    var lights: MGManagerLight
): MGIDrawer {

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
        directionalLight.draw()
        meshes.forEach {
            it.draw()
        }
        lights.draw()


        shaderTrigger.use()
        camera.draw(
            shaderTrigger
        )
        triggers.draw()
        triggersLight.forEach {
            it.draw()
        }
    }

}