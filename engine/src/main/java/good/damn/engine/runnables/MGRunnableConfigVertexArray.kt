package good.damn.engine.runnables

import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.runnables.MGIRunnableBounds
import java.nio.Buffer
import java.nio.FloatBuffer

class MGRunnableConfigVertexArray(
    private val arrayVertex: MGArrayVertexConfigurator,
    private val vertexBuffer: FloatBuffer,
    private val indicesBuffer: Buffer,
    private val pointerAttribute: MGPointerAttribute
): MGIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        arrayVertex.configure(
            vertexBuffer,
            indicesBuffer,
            pointerAttribute
        )
    }
}