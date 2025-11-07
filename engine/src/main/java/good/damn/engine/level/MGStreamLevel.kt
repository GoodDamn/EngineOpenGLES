package good.damn.engine.level

import android.util.Log
import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.MGArrayVertexInstanced
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.utils.MGUtilsBuffer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.FloatBuffer

class MGStreamLevel {

    companion object {
        fun read(
            input: InputStream,
            poolTextures: MGPoolTextures
        ): Array<MGMMeshInstance>? {
            val bufferedReader = BufferedReader(
                InputStreamReader(
                    input
                )
            )

            val meshesCount = bufferedReader.readLineValueInt()
                ?: return null

            val output = Array(
                meshesCount
            ) {
                val meshNameCount = bufferedReader
                    .readLine()!!
                    .split("\\s+".toRegex())

                val meshCount = meshNameCount[1]
                    .toInt()

                val meshName = meshNameCount[0]

                val modelMatrices = Array(
                    meshCount
                ) {
                    MGMatrixTransformationNormal(
                        MGMatrixScaleRotation()
                    ).apply {
                        model.run {
                            val strPosition = bufferedReader
                                .readLine()!!
                                .split("\\s+".toRegex())

                            setPosition(
                                strPosition.getOrNull(0)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(1)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(2)?.toFloatOrNull() ?: 0f,
                            )

                            val scale = strPosition.getOrNull(3)?.toFloatOrNull() ?: 0f
                            setScale(
                                scale,
                                scale,
                                scale
                            )

                            setRotation(
                                strPosition.getOrNull(4)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(5)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(6)?.toFloatOrNull() ?: 0f,
                            )

                            invalidatePosition()
                            invalidateScaleRotation()
                        }

                        normal.run {
                            calculateInvertModel()
                            calculateNormalMatrix()
                        }
                    }
                }

                val obj = MGObject3d.createFromAssets(
                    "objs/$meshName"
                )?.get(0)!!

                val material = MGMaterial.createWithPath(
                    poolTextures,
                    obj.texturesDiffuseFileName?.get(0),
                    obj.texturesMetallicFileName?.get(0),
                    obj.texturesEmissiveFileName?.get(0)
                )

                val vertexArray = MGArrayVertexInstanced()
                vertexArray.configure(
                    obj.vertices,
                    obj.indices
                )

                val matrices = convertMatricesToBuffer(
                    modelMatrices
                )

                vertexArray.setupMatrixBuffer(
                    meshCount,
                    matrices.model,
                    matrices.rotation
                )

                vertexArray.setupInstanceDrawing(
                    MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_MODEL,
                    MGArrayVertexInstanced.INDEX_BUFFER_MODEL
                )

                vertexArray.setupInstanceDrawing(
                    MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_ROTATION,
                    MGArrayVertexInstanced.INDEX_BUFFER_ROTATION
                )

                return@Array MGMMeshInstance(
                    vertexArray,
                    material,
                    modelMatrices
                )
            }
            bufferedReader.close()

            return output
        }

        private fun BufferedReader.readLineValueInt() =
            readLine().toIntOrNull()

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
}