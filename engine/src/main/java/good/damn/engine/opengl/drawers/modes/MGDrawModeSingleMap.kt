package good.damn.engine.opengl.drawers.modes

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderSingleMap
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawModeSingleMap(
    private val shaderMap: MGShaderSingleMap,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>
): MGIDrawer {

    override fun draw() {
        shaderMap.use()
        camera.draw(
            shaderMap
        )
        meshes.forEach {
            it.drawSingleTexture(
                shaderMap,
                shaderMap
            )
        }
    }
}