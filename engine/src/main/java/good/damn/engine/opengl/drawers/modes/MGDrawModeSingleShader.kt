package good.damn.engine.opengl.drawers.modes

import good.damn.engine.models.MGMInformator
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
    private val informator: MGMInformator
): MGIDrawer {

    override fun draw() {
        shader.single.run {
            use()
            informator.camera.draw(
                this
            )

            informator.meshSky.drawVertices(
                this
            )

            informator.meshes.forEach {
                it.value.forEach {
                    it.drawVertices(
                        this
                    )
                }
            }
        }

        shader.instanced.run {
            use()
            informator.camera.draw(
                this
            )
            informator.meshesInstanced.forEach {
                it.value.forEach {
                    it.drawVertices()
                }
            }
        }
    }
}