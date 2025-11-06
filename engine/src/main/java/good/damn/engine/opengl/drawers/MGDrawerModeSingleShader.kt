package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawerModeSingleShader(
    private val shaderSingle: MGShaderSingleMode,
    private val sky: MGDrawerMeshTexture,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshTexture>
): MGIDrawer {

    override fun draw() {
        shaderSingle.use()
        camera.draw(
            shaderSingle
        )
        sky.draw()
        meshes.forEach {
            it.draw()
        }
    }
}