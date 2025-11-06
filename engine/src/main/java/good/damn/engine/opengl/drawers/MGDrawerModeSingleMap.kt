package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawerModeSingleMap(
    private val shaderMap: MGShaderSingleMap,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshTexture>
): MGIDrawer {

    override fun draw() {
        shaderMap.use()
        camera.draw(
            shaderMap
        )
        meshes.forEach {
            it.draw()
        }
    }
}