package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.shaders.MGShaderWireframe
import java.util.LinkedList

class MGDrawerModeWireframe(
    private val shaderWireframe: MGShaderWireframe,
    private val sky: MGSky,
    private val camera: MGCamera,
    private val meshes: LinkedList<MGDrawerMeshSwitch>
): MGIDrawer {

    override fun draw() {
        shaderWireframe.use()
        camera.draw()
        sky.draw()
        meshes.forEach {
            it.draw()
        }
    }
}