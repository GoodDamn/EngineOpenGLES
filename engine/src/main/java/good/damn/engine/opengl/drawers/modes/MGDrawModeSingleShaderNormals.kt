package good.damn.engine.opengl.drawers.modes

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleModeNormals
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawModeSingleShaderNormals(
    private val shader: MGMShader<MGShaderSingleModeNormals, MGShaderSingleModeInstanced>,
    private val sky: MGDrawerMeshTextureSwitch,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>,
    private val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>
): MGIDrawer {

    override fun draw() {
        shader.single.run {
            use()
            camera.draw(this)
            sky.drawNormals(this)
            sky.drawVertices(this)
            meshes.forEach {
                it.drawNormals(this)
                it.drawVertices(this)
            }
        }

        shader.instanced.run {
            use()
            camera.draw(this)
            meshesInstanced.forEach {
                it.drawVertices()
            }
        }


    }
}