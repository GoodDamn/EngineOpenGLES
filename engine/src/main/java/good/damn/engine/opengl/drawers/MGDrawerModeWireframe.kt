package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.shaders.MGShaderWireframe
import java.util.LinkedList

class MGDrawerModeWireframe(
    private val shaderWireframe: MGShaderWireframe,
    private val sky: MGIDrawer,
    private val camera: MGCamera,
    private val meshes: LinkedList<Pair<MGDrawerModeSwitch, MGDrawerPositionEntity>>
): MGIDrawer {

    override fun draw() {
        shaderWireframe.use()
        camera.draw()
        sky.draw()
        meshes.forEach {
            it.second.draw()
        }
    }
}