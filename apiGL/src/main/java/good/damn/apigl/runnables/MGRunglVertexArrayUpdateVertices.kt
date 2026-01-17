package good.damn.apigl.runnables

import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.common.COIRunnableBounds
import java.nio.FloatBuffer

class MGRunglVertexArrayUpdateVertices(
    private val arrayVertex: GLArrayVertexConfigurator,
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