package good.damn.engine.opengl.runnables.misc

import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import java.nio.Buffer
import java.nio.FloatBuffer

class MGRunglConfigVertexArray(
    private val arrayVertex: MGArrayVertexConfigurator,
    private val vertexBuffer: FloatBuffer,
    private val indicesBuffer: Buffer,
    private val pointerAttribute: MGPointerAttribute
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