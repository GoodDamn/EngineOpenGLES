package good.damn.engine.opengl.runnables.misc

import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import java.nio.FloatBuffer

class MGRunglVertexArrayUpdateVertices(
    private val arrayVertex: MGArrayVertexConfigurator,
    private val vertexBuffer: FloatBuffer,
): COIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        arrayVertex.bindVertexBuffer()
        arrayVertex.sendVertexBufferData(
            vertexBuffer
        )
        arrayVertex.unbind()
    }
}