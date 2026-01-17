package good.damn.engine.utils

import good.damn.common.COHandlerGl
import good.damn.engine.models.MGMInstanceArray
import good.damn.apigl.arrays.MGArrayVertexConfigurator
import good.damn.apigl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import good.damn.apigl.runnables.MGRunglGenVertexArrayInstanced
import java.nio.Buffer
import java.nio.FloatBuffer

object MGUtilsInstancedMesh {

    @JvmStatic
    fun createVertexArrayInstance(
        config: MGEnumArrayVertexConfiguration,
        bufferVertices: FloatBuffer,
        bufferIndices: Buffer,
        modelMatrices: List<
            COMatrixTransformationNormal<
                COMatrixScaleRotation
                >
            >,
        handlerGl: COHandlerGl
    ): MGMInstanceArray {
        val configurator = good.damn.apigl.arrays.MGArrayVertexConfigurator(
            config
        )

        val vertexArray = good.damn.apigl.arrays.MGArrayVertexInstanced(
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