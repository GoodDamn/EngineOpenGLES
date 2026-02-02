package good.damn.apigl.runnables

import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.GLArrayVertexInstanced
import good.damn.common.COIRunnableBounds
import java.nio.Buffer
import java.nio.FloatBuffer

class GLRunglGenVertexArrayInstanced(
    private val vertexArray: GLArrayVertexInstanced,
    private val configurator: GLArrayVertexConfigurator,
    private val bufferVertices: FloatBuffer,
    private val bufferIndices: Buffer,
    private val matrixModel: FloatBuffer,
    private val matrixRotation: FloatBuffer,
    private val matrixSize: Int
): COIRunnableBounds {

    override fun run(
        width: Int,
        height: Int
    ) {
        configurator.configure(
            bufferVertices,
            bufferIndices,
            good.damn.apigl.arrays.pointers.GLPointerAttribute.default32
        )

        vertexArray.setupMatrixBuffer(
            matrixSize,
            matrixModel,
            matrixRotation
        )

        vertexArray.setupInstanceDrawing(
            good.damn.apigl.arrays.GLArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_MODEL,
            good.damn.apigl.arrays.GLArrayVertexInstanced.INDEX_BUFFER_MODEL
        )

        vertexArray.setupInstanceDrawing(
            good.damn.apigl.arrays.GLArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_ROTATION,
            good.damn.apigl.arrays.GLArrayVertexInstanced.INDEX_BUFFER_ROTATION
        )
    }
}