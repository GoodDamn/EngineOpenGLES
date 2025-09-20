package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import good.damn.engine.opengl.triggers.MGTriggerBaseDebug
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue

data class MGDrawerModeOpaque(
    var shaderSky: MGShaderSkySphere,
    var shaderOpaque: MGShaderDefault,
    var shaderTrigger: MGShaderSingleMode,
    var sky: MGMesh,
    var camera: MGCamera,
    var directionalLight: MGDrawerLightDirectional,
    var meshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>,
    var triggers: ConcurrentLinkedQueue<MGTriggerBaseDebug>
): MGIDrawer {

    private val meshNull = MGDrawerPositionEntity(
        object: MGIDrawer { override fun draw() {} },
        shaderTrigger,
        MGMMatrix()
    )

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



        shaderTrigger.use()
        meshNull.draw()
        camera.draw(
            shaderTrigger
        )
        triggers.forEach {
            it.draw()
        }
    }

}