package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_NO_ERROR
import android.opengl.GLES30.glGetError
import android.util.Log
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.shaders.MGShaderSingleMode
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue

class MGDrawerModeSingleShader(
    private val shaderWireframe: MGShaderSingleMode,
    private val sky: MGMesh,
    private val camera: MGCamera,
    private val meshes: ConcurrentLinkedQueue<MGDrawerMeshSwitch>
): MGIDrawer {

    override fun draw() {
        shaderWireframe.use()
        camera.draw(
            shaderWireframe
        )
        sky.draw()
        meshes.forEach {
            it.draw()
        }
    }
}