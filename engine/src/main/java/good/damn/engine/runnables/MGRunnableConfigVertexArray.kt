package good.damn.engine.runnables

import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import java.nio.Buffer
import java.nio.FloatBuffer

class MGRunnableConfigVertexArray(
    private val arrayVertex: MGArrayVertexConfigurator,
    private val vertexBuffer: FloatBuffer,
    private val indicesBuffer: Buffer,
    private val stride: Int
): Runnable {

    override fun run() {
        arrayVertex.configure(
            vertexBuffer,
            indicesBuffer,
            stride
        )
    }
}