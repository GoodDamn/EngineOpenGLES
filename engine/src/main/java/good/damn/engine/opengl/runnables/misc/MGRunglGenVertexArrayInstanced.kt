package good.damn.engine.opengl.runnables.misc

import good.damn.common.COIRunnableBounds
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import java.nio.Buffer
import java.nio.FloatBuffer

class MGRunglGenVertexArrayInstanced(
    private val vertexArray: MGArrayVertexInstanced,
    private val configurator: MGArrayVertexConfigurator,
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
            MGPointerAttribute.default32
        )

        vertexArray.setupMatrixBuffer(
            matrixSize,
            matrixModel,
            matrixRotation
        )

        vertexArray.setupInstanceDrawing(
            MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_MODEL,
            MGArrayVertexInstanced.INDEX_BUFFER_MODEL
        )

        vertexArray.setupInstanceDrawing(
            MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_ROTATION,
            MGArrayVertexInstanced.INDEX_BUFFER_ROTATION
        )
    }
}