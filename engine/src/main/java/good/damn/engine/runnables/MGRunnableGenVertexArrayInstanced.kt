package good.damn.engine.runnables

import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import java.nio.Buffer
import java.nio.FloatBuffer

class MGRunnableGenVertexArrayInstanced(
    private val vertexArray: MGArrayVertexInstanced,
    private val configurator: MGArrayVertexConfigurator,
    private val bufferVertices: FloatBuffer,
    private val bufferIndices: Buffer,
    private val modelMatrices: Array<
        MGMatrixTransformationNormal<
            MGMatrixScaleRotation
        >
    >,
    private val matrixModel: FloatBuffer,
    private val matrixRotation: FloatBuffer
): Runnable {

    override fun run() {
        configurator.configure(
            bufferVertices,
            bufferIndices,
            MGPointerAttribute.default32
        )

        vertexArray.setupMatrixBuffer(
            modelMatrices.size,
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