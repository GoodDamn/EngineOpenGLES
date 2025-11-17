package good.damn.engine.opengl.drawers.modes

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import good.damn.engine.opengl.shaders.MGShaderSingleMapInstanced
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawModeSingleMap(
    private val shader: MGMShader<MGShaderSingleMap, MGShaderSingleMapInstanced>,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>,
    private val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>
): MGIDrawer {

    override fun draw() {
        shader.single.run {
            use()
            camera.draw(
                this
            )
            meshes.forEach {
                it.drawSingleTexture(
                    this,
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
                it.drawSingleTexture(
                    this
                )
            }
        }
    }
}