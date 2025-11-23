package good.damn.engine.runnables

import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import java.nio.Buffer
import java.nio.FloatBuffer

class MGRunnableConfigVertexArray(
    private val arrayVertex: MGArrayVertexConfigurator,
    private val vertexBuffer: FloatBuffer,
    private val indicesBuffer: Buffer,
    private val pointerAttribute: MGPointerAttribute
): Runnable {

    override fun run() {
        arrayVertex.configure(
            vertexBuffer,
            indicesBuffer,
            pointerAttribute
        )
    }
}