package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.shaders.MGShaderDefault
import good.damn.engine.opengl.shaders.MGShaderSkySphere
import java.util.LinkedList

data class MGDrawerModeOpaque(
    var shaderSky: MGShaderSkySphere,
    var shaderOpaque: MGShaderDefault,
    var sky: MGDrawerPositionEntity,
    var camera: MGCamera,
    var directionalLight: MGDrawerLightDirectional,
    var meshes: LinkedList<MGDrawerPositionEntity>
): MGIDrawer {

    override fun draw() {
        shaderSky.use()
        camera.draw()
        sky.draw()

        shaderOpaque.use()
        camera.draw()
        directionalLight.draw()

        meshes.forEach {
            it.draw()
        }
    }

}