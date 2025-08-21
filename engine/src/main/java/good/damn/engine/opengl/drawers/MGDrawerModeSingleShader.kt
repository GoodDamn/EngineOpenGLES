package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import java.util.LinkedList

class MGDrawerModeSingleShader(
    private val shaderWireframe: MGShaderSingleMode,
    private val sky: MGMesh,
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