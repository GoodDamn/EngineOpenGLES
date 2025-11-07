package good.damn.engine.opengl.drawers.modes

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import good.damn.engine.opengl.shaders.MGShaderSingleModeInstanced
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawModeSingleShader(
    private val shader: MGMShader<MGShaderSingleMode, MGShaderSingleModeInstanced>,
    private val sky: MGDrawerMeshTextureSwitch,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>,
    private val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>,
): MGIDrawer {

    override fun draw() {
        shader.single.run {
            use()
            camera.draw(
                this
            )

            sky.drawVertices(
                this
            )

            meshes.forEach {
                it.drawVertices(
                    this
                )
            }
        }

        shader.instanced.run {
            use()
            camera.draw(
                this
            )
            meshesInstanced.forEach {
                it.drawVertices()
            }
        }
    }
}