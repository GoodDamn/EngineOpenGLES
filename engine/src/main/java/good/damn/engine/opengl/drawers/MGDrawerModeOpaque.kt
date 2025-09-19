package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue

data class MGDrawerModeOpaque(
    var shaderSky: MGShaderSkySphere,
    var shaderOpaque: MGShaderDefault,
    var sky: MGMesh,
    var camera: MGCamera,
    var directionalLight: MGDrawerLightDirectional,
    var meshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>
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
    }

}