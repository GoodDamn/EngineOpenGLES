package good.damn.apigl.runnables

import good.damn.common.COIRunnableBounds
import java.nio.Buffer
import java.nio.FloatBuffer

class MGRunglConfigVertexArray(
    private val arrayVertex: good.damn.apigl.arrays.GLArrayVertexConfigurator,
    private val vertexBuffer: FloatBuffer,
    private val indicesBuffer: Buffer,
    private val pointerAttribute: good.damn.apigl.arrays.pointers.GLPointerAttribute
): COIRunnableBounds {

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