package good.damn.engine.opengl.drawers.modes

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawModeSingleShaderNormals(
    private val shaderSingle: MGShaderSingleModeNormals,
    private val sky: MGDrawerMeshTextureSwitch,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>
): MGIDrawer {

    override fun draw() {
        shaderSingle.use()
        camera.draw(
            shaderSingle
        )
        sky.drawNormals(
            shaderSingle
        )
        sky.drawVertices(
            shaderSingle
        )
        meshes.forEach {
            it.drawNormals(
                shaderSingle
            )
            it.drawVertices(
                shaderSingle
            )
        }
    }
}