package good.damn.engine2.utils

import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.GLArrayVertexInstanced
import good.damn.apigl.enums.GLEnumArrayVertexConfiguration
import good.damn.common.COHandlerGl
import good.damn.engine2.models.MGMInstanceArray
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import good.damn.engine.MGUtilsBuffer
import java.nio.Buffer
import java.nio.FloatBuffer

object MGUtilsInstancedMesh {

    @JvmStatic
    fun createVertexArrayInstance(
        config: GLEnumArrayVertexConfiguration,
        bufferVertices: FloatBuffer,
        bufferIndices: Buffer,
        modelMatrices: List<
            COMatrixTransformationNormal<
                COMatrixScaleRotation
                >
            >,
        handlerGl: COHandlerGl
    ): MGMInstanceArray {
        val configurator = GLArrayVertexConfigurator(
            config
        )

        val vertexArray = GLArrayVertexInstanced(
            configurator
        )

        val matrices = convertMatricesToBuffer(
            modelMatrices
        )

        handlerGl.post(
            good.damn.apigl.runnables.MGRunglGenVertexArrayInstanced(
                vertexArray,
                configurator,
                bufferVertices,
                bufferIndices,
                matrices.model,
                matrices.rotation,
                modelMatrices.size
            )
        )

        return MGMInstanceArray(
            vertexArray,
            modelMatrices
        )
    }

    private inline fun convertMatricesToBuffer(
        v: List<
            COMatrixTransformationNormal<
                COMatrixScaleRotation
                >
            >
    ): MGMatrixBuffer {
        var i = 0
        val outputModel = MGUtilsBuffer.allocateFloat(
            v.size * 16
        )
        val outputRotation = MGUtilsBuffer.allocateFloat(
            outputModel.capacity()
        )

        v.forEach {
            for (indexMat in it.model.model.indices) {
                outputModel.put(
                    i,
                    it.model.model[indexMat]
                )
                outputRotation.put(
                    i,
                    it.normal.normalMatrix[indexMat]
                )
                i++
            }
        }

        outputModel.position(0)
        outputRotation.position(0)

        return MGMatrixBuffer(
            outputModel,
            outputRotation
        )
    }

    private data class MGMatrixBuffer(
        val model: FloatBuffer,
        val rotation: FloatBuffer
    )
}