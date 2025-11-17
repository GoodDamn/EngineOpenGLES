package good.damn.engine.loaders.mesh

import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGRunnableGenVertexArrayInstanced
import good.damn.engine.utils.MGUtilsBuffer
import java.nio.Buffer
import java.nio.FloatBuffer

interface MGILoaderMesh<T> {

    companion object {
        fun createVertexArrayInstance(
            config: MGEnumArrayVertexConfiguration,
            bufferVertices: FloatBuffer,
            bufferIndices: Buffer,
            modelMatrices: Array<
                MGMatrixTransformationNormal<
                    MGMatrixScaleRotation
                    >
                >,
            material: MGMaterial,
            handlerGl: MGHandlerGl,
            enableCullFace: Boolean
        ): MGMMeshInstance {
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
                    modelMatrices,
                    matrices.model,
                    matrices.rotation
                )
            )

            return MGMMeshInstance(
                vertexArray,
                material,
                enableCullFace,
                modelMatrices
            )
        }

        private inline fun convertMatricesToBuffer(
            v: Array<
                MGMatrixTransformationNormal<
                    MGMatrixScaleRotation
                    >
                >
        ): MGMatrixBuffer {
            var i = 0
            val outputModel = FloatArray(
                v.size * 16
            )
            val outputRotation = FloatArray(
                outputModel.size
            )

            v.forEach {
                for (indexMat in it.model.model.indices) {
                    outputModel[i] = it.model.model[indexMat]
                    outputRotation[i] = it.normal.normalMatrix[indexMat]
                    i++
                }
            }

            return MGMatrixBuffer(
                MGUtilsBuffer.createFloat(
                    outputModel
                ),
                MGUtilsBuffer.createFloat(
                    outputRotation
                )
            )
        }

        private data class MGMatrixBuffer(
            val model: FloatBuffer,
            val rotation: FloatBuffer
        )
    }

    fun loadMeshInstance(
        it: T
    ): MGMMeshInstance?
}