package good.damn.engine.utils

import good.damn.common.COHandlerGl
import good.damn.engine.models.MGMInstanceArray
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.common.matrices.MGMatrixScaleRotation
import good.damn.common.matrices.MGMatrixTransformationNormal
import good.damn.engine.runnables.MGRunnableGenVertexArrayInstanced
import java.nio.Buffer
import java.nio.FloatBuffer

object MGUtilsInstancedMesh {

    @JvmStatic
    fun createVertexArrayInstance(
        config: MGEnumArrayVertexConfiguration,
        bufferVertices: FloatBuffer,
        bufferIndices: Buffer,
        modelMatrices: List<
            MGMatrixTransformationNormal<
                MGMatrixScaleRotation
                >
            >,
        handlerGl: COHandlerGl
    ): MGMInstanceArray {
        val configurator = MGArrayVertexConfigurator(
            config
        )

        val vertexArray = MGArrayVertexInstanced(
            configurator
        )

        val matrices = convertMatricesToBuffer(
            modelMatrices
        )

        handlerGl.post(
            MGRunnableGenVertexArrayInstanced(
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
            MGMatrixTransformationNormal<
                MGMatrixScaleRotation
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