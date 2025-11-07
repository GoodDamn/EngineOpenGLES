package good.damn.engine.opengl.drawers.modes

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawModeSingleShader(
    private val shaderSingle: MGShaderSingleMode,
    private val sky: MGDrawerMeshTextureSwitch,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>
): MGIDrawer {

    override fun draw() {
        shaderSingle.use()
        camera.draw(
            shaderSingle
        )
        sky.drawVertices(
            shaderSingle
        )
        meshes.forEach {
            it.drawVertices(
                shaderSingle
            )
        }
    }
}